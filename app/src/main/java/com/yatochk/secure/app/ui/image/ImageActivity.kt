package com.yatochk.secure.app.ui.image

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.MediaActivity
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.scaleDown
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject
import kotlin.math.min

class ImageActivity : MediaActivity() {

    companion object {
        private const val IMAGE = "opened_image"

        fun intent(context: Context, image: Image) =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(IMAGE, image)
            }

    }

    private val viewModel: ImageViewModel by viewModels { viewModelFactory }

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
                viewModel.initMedia(it as Image)
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
                try {
                    val fullSizeBitmap = BitmapFactory.decodeByteArray(
                        it,
                        0,
                        it.size
                    )
                    val displaySize = Point()
                    windowManager.defaultDisplay.getSize(displaySize)
                    gallery_image.setImageBitmap(
                        fullSizeBitmap.scaleDown(min(displaySize.x, displaySize.y).toFloat(), true)
                    )
                } catch (e: Throwable) {
                    showErrorToast(this@ImageActivity, getString(R.string.error_display_image))
                }
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
            mediaError.observe(this@ImageActivity) {
                showErrorToast(this@ImageActivity, it)
            }
        }
    }

    override fun onAnimationEnd() {
        viewModel.animationEnd()
    }

    override val mediaView: View?
        get() = gallery_image

}