package com.yatochk.secure.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.transition.TransitionManager
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.getbase.floatingactionbutton.FloatingActionButton
import com.yatochk.secure.app.BuildConfig
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.browser.BrowserFragment
import com.yatochk.secure.app.ui.contact.ContactFragment
import com.yatochk.secure.app.ui.gallery.GalleryFragment
import com.yatochk.secure.app.ui.gallery.GalleryMenuViewModel
import com.yatochk.secure.app.ui.notes.NotesFragment
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.showErrorToast
import com.yatochk.secure.app.utils.toPath
import com.yatochk.secure.app.utils.toTimeString
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


class MainActivity : BaseActivity() {

    companion object {
        private const val TAKE_PHOTO = 0
        private const val TAKE_VIDEO = 1
        private const val PICK_PHOTO = 2
        private const val PICK_VIDEO = 3
    }

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    private val galleryMenuViewModel: GalleryMenuViewModel by viewModels { viewModelFactory }

    private val galleryFragment by lazy { GalleryFragment() }
    private val notesFragment by lazy { NotesFragment() }
    private val browserFragment by lazy { BrowserFragment() }

    private lateinit var imageName: String
    private lateinit var videoName: String

    private val mainSet = ConstraintSet()
    private val pickerSet = ConstraintSet()
    private var isPickerOpened = false

    private fun initSets() {
        mainSet.clone(container)
        pickerSet.clone(this, R.layout.main_pick_media_type)
    }

    private fun animatePicker(open: Boolean) {
        TransitionManager.beginDelayedTransition(container)
        (if (open) pickerSet else mainSet).applyTo(container)
    }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSets()
        goToFragment(galleryFragment)
        galleryFloatingMenu()
        nav_view.setOnNavigationItemSelectedListener {
            goToFragment(
                when (it.itemId) {
                    R.id.navigation_gallery -> {
                        galleryFragment
                    }
                    R.id.navigation_contact -> {
                        ContactFragment()
                    }
                    R.id.navigation_notes -> {
                        notesFragment
                    }
                    R.id.navigation_internet -> {
                        browserFragment
                    }
                    else -> throw IllegalStateException("Not implement FAButton for ${it.itemId}")
                }
            )
            true
        }
        initObservers()
    }

    private fun replaceFloatingMenu(fragment: Fragment) {
        floating_menu_gallery.isVisible = false
        when (fragment) {
            is GalleryFragment -> floating_menu_gallery.isVisible = true
        }
    }

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container_fragment,
                fragment
            )
            .commit()
        replaceFloatingMenu(fragment)
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private val photoButton by lazy {
        FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_camera)
            setOnClickListener {
                galleryMenuViewModel.clickPhoto()
            }
            colorNormal = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
        }
    }
    private val galleryButton by lazy {
        FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_gallery_locale)
            setOnClickListener {
                galleryMenuViewModel.clickGallery()
            }
            colorNormal = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
        }
    }

    private fun galleryFloatingMenu() {
        floating_menu_gallery.addButton(galleryButton)
        floating_menu_gallery.addButton(photoButton)
    }

    private fun initObservers() {
        initMainObservers()
        initGalleryMenuObservers()
    }

    private fun initGalleryMenuObservers() {
        with(galleryMenuViewModel) {
            openTypePicker.observe(this@MainActivity) {
                animatePicker(!isPickerOpened)
                isPickerOpened = !isPickerOpened
            }

            openCamera.observe(this@MainActivity) {
                val imagePath = File(ImageSecureController.regularPath)
                imagePath.mkdirs()
                imageName = "Photo_${Date().toTimeString()}.jpg"

                val photoUri = FileProvider.getUriForFile(
                    this@MainActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    File(imagePath, imageName)
                )

                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivityForResult(takePicture, TAKE_PHOTO)
            }

            openVideoCamera.observe(this@MainActivity) {
                val videoPath = File(ImageSecureController.regularPath)
                videoPath.mkdirs()
                videoName = "Video_${Date().toTimeString()}.mp4"

                val videoUri = FileProvider.getUriForFile(
                    this@MainActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    File(videoPath, videoName)
                )

                val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivityForResult(takeVideoIntent, TAKE_VIDEO)
            }

            openGallery.observe(this@MainActivity) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, PICK_PHOTO)
            }

            openVideoGallery.observe(this@MainActivity) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, PICK_VIDEO)
            }
        }
    }

    private fun initMainObservers() {
        with(mainViewModel) {
            scanImage.observe(this@MainActivity) {
                scanMedia(it)
            }

            showImageError.observe(this@MainActivity) {
                showErrorToast(
                    this@MainActivity,
                    when (it) {
                        ImageErrorType.ADD_PHOTO -> {
                            getString(R.string.error_photo)
                        }
                        ImageErrorType.ADD_IMAGE -> {
                            getString(R.string.error_gallery)
                        }
                        ImageErrorType.ENCRYPT_IMAGE -> {
                            getString(R.string.error_encrypt_image)
                        }
                        ImageErrorType.DELETE_IMAGE -> {
                            getString(R.string.error_delete_image)
                        }
                        ImageErrorType.TO_GALLERY -> {
                            getString(R.string.error_to_gallery)
                        }
                    }
                )
            }

        }
    }

    private fun checkPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                10
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            TAKE_PHOTO -> {
                mainViewModel.receivedMedia(imageName)
            }
            TAKE_VIDEO -> {
                mainViewModel.receivedMedia(videoName)
            }
            PICK_PHOTO -> {
                data?.data?.toPath(this)?.also {
                    mainViewModel.receivedGalleryImage(it)
                }
            }
            PICK_VIDEO -> {

            }
        }
    }
}

