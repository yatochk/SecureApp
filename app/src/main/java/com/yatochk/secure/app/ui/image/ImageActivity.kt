package com.yatochk.secure.app.ui.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.main.ErrorType
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject

class ImageActivity : BaseActivity() {

    companion object {
        private const val DURATION_ANIMATION = 500L
        private const val IMAGE = "opened_image"

        fun intent(context: Context, image: Image) =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(IMAGE, image)
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
        intent.getSerializableExtra(IMAGE)?.also {
            if (savedInstanceState == null) {
                viewModel.initImage(it as Image)
            }
        }
        button_image_delete.setOnClickListener {
            viewModel.onDelete()
        }
        button_image_rename.setOnClickListener {
            viewModel.clickRename()
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
            delete.observe(this@ImageActivity) {
                deleteAnimation()
            }
            toGallery.observe(this@ImageActivity) {
                toGalleryAnimation()
            }
            openRename.observe(this@ImageActivity) {
                openRenameAnimation()
            }
            scanImage.observe(this@ImageActivity) {
                scanMedia(it)
            }
            finish.observe(this@ImageActivity) {
                finish()
            }
            error.observe(this@ImageActivity) {
                if (it == ErrorType.TO_GALLERY) {
                    showErrorToast(this@ImageActivity, getString(R.string.error_to_gallery))
                }
            }
        }
    }

    private fun openRenameAnimation() {

    }

    private fun toGalleryAnimation() {
        gallery_image.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .scaleX(0f)
            .scaleY(0f)
            .withEndAction {
                viewModel.animationEnd()
            }
            .start()

        image_to_gallery.animate()
            .alpha(1f)
            .setDuration(DURATION_ANIMATION)
            .start()

        container_image_options.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .start()
    }

    private fun deleteAnimation() {
        gallery_image.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .scaleX(0f)
            .scaleY(0f)
            .withEndAction {
                viewModel.animationEnd()
            }
            .start()

        image_deleted.animate()
            .alpha(1f)
            .setDuration(DURATION_ANIMATION)
            .start()

        container_image_options.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .start()
    }

}