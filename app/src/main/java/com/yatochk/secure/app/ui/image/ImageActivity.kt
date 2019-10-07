package com.yatochk.secure.app.ui.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject

class ImageActivity : BaseActivity() {

    companion object {

        private const val IMAGE_PATH = "opened_image"

        fun intent(context: Context, path: String) =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(IMAGE_PATH, path)
            }

    }

    private val viewModel: ImageVIewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var imageSecureController: ImageSecureController

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        intent.getStringExtra(IMAGE_PATH)?.also {
            if (savedInstanceState == null) {
                viewModel.initImagePath(it)
            }
        }
        button_image_delete.setOnClickListener {
            viewModel.onDelete()
        }
        button_image_rename.setOnClickListener {
            viewModel.onRename()
        }
        button_image_upload.setOnClickListener {
            viewModel.onToGallery()
        }
        observers()
    }

    private fun observers() {
        with(viewModel) {
            image.observe(this@ImageActivity) {
                gallery_image.setImageBitmap(it)
            }
            closeWithDelete.observe(this@ImageActivity) {
                deleteAnimation()
                finish()
            }
        }
    }

    private fun deleteAnimation() {

    }

}