package com.yatochk.secure.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
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
        private const val PICK_IMAGE = 1
    }

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private val galleryFragment by lazy { GalleryFragment() }
    private val contactFragment by lazy { ContactFragment() }
    private val notesFragment by lazy { NotesFragment() }

    private val browserFragment by lazy { BrowserFragment() }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goToFragment(galleryFragment)
        galleryFloatingMenu()
        nav_view.setOnNavigationItemSelectedListener {
            floating_menu.isVisible = it.itemId != R.id.navigation_internet
            goToFragment(
                when (it.itemId) {
                    R.id.navigation_gallery -> {
                        galleryFragment
                    }
                    R.id.navigation_contact -> {
                        contactFragment
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

    private fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container_fragment,
                fragment
            )
            .commit()
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private val photoButton by lazy {
        FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_camera)
            setOnClickListener {
                viewModel.clickPhoto()
            }
            colorNormal = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
        }
    }
    private val galleryButton by lazy {
        FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_gallery_locale)
            setOnClickListener {
                viewModel.clickGallery()
            }
            colorNormal = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
            colorPressed = ContextCompat.getColor(this@MainActivity, R.color.colorAccent)
        }
    }

    private fun galleryFloatingMenu() {
        floating_menu.addButton(galleryButton)
        floating_menu.addButton(photoButton)
    }

    private lateinit var imageName: String

    private fun initObservers() {
        with(viewModel) {
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

            openGallery.observe(this@MainActivity) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, PICK_IMAGE)
            }

            scanImage.observe(this@MainActivity) {
                scanMedia(it)
            }

            showError.observe(this@MainActivity) {
                showErrorToast(
                    this@MainActivity,
                    when (it) {
                        ErrorType.ADD_PHOTO -> {
                            getString(R.string.error_photo)
                        }
                        ErrorType.ADD_IMAGE -> {
                            getString(R.string.error_gallery)
                        }
                        ErrorType.ENCRYPT_IMAGE -> {
                            getString(R.string.error_encrypt)
                        }
                        ErrorType.DELETE_IMAGE -> {
                            getString(R.string.error_delete)
                        }
                        ErrorType.TO_GALLERY -> {
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
                viewModel.receivedPhoto(imageName)
            }
            PICK_IMAGE -> {
                data?.data?.toPath(this)?.also {
                    viewModel.receivedGalleryImage(it)
                }
            }
        }
    }
}

