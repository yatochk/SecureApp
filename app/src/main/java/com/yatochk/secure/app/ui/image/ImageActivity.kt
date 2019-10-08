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
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject

class ImageActivity : BaseActivity() {

    companion object {
        private const val DURATION_END = 300L
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
            delete.observe(this@ImageActivity) {
                deleteAnimation()
            }
            finish.observe(this@ImageActivity) {
                finish()
            }
        }
    }

    private fun deleteAnimation() {
        gallery_image.animate()
            .alpha(0f)
            .setDuration(DURATION_END)
            .scaleX(0f)
            .scaleY(0f)
            .withEndAction {
                viewModel.animationEnd()
            }
            .start()

        image_deleted.animate()
            .alpha(1f)
            .setDuration(DURATION_END)
            .start()

        container_image_options.animate()
            .alpha(0f)
            .setDuration(DURATION_END)
            .start()
    }

}