package com.yatochk.secure.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.getbase.floatingactionbutton.FloatingActionButton
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    companion object {
        private const val TAKE_PHOTO = 0
        private const val PICK_IMAGE = 1
    }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)
        galleryFloatingMenu()
        nav_view.setOnNavigationItemSelectedListener {
            floating_menu.isVisible = it.itemId != R.id.navigation_internet
            when (it.itemId) {
                R.id.navigation_gallery -> {
                    true
                }
                R.id.navigation_contact -> {
                    true
                }
                R.id.navigation_notes -> {
                    true
                }
                R.id.navigation_internet -> {
                    true
                }
                else -> throw IllegalStateException("Not implement FAButton for ${it.itemId}")
            }
        }
        initObservers()
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
        }
    }
    private val galleryButton by lazy {
        FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_gallery_locale)
            setOnClickListener {
                viewModel.clickGallery()
            }
        }
    }

    private fun galleryFloatingMenu() {
        floating_menu.addButton(galleryButton)
        floating_menu.addButton(photoButton)
    }

    private fun initObservers() {
        with(viewModel) {
            openCamera.observe(this@MainActivity) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, TAKE_PHOTO)
            }

            openGallery.observe(this@MainActivity) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, PICK_IMAGE)
            }

            showError.observe(this@MainActivity) {
                when (it) {
                    ErrorType.ADD_IMAGE -> {
                        //TODO
                    }
                    ErrorType.ADD_PHOTO -> {
                        //TODO
                    }
                }
            }

            showSuccess.observe(this@MainActivity) {
                when (it) {
                    SuccessType.ADD_PHOTO -> {
                        //TODO
                    }
                    SuccessType.ADD_IMAGE -> {
                        //TODO
                    }
                }
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
        if (resultCode != RESULT_OK || data == null) return
        when (requestCode) {
            TAKE_PHOTO -> {
                (data.extras?.get("data") as? Bitmap)?.also {
                    viewModel.receivedPhoto(it)
                }
            }
            PICK_IMAGE -> {
                val selectedImage = data.data as Uri
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(
                    selectedImage,
                    filePathColumn,
                    null,
                    null,
                    null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    viewModel.receivedGalleryImage(picturePath)
                }
            }
        }
    }
}
