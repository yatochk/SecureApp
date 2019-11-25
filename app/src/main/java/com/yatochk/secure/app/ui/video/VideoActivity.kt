package com.yatochk.secure.app.ui.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.Image
import com.yatochk.secure.app.ui.MediaActivity
import com.yatochk.secure.app.ui.main.ImageErrorType
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : MediaActivity() {

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    companion object {
        private const val VIDEO = "opened_video"

        fun intent(context: Context, image: Image) =
            Intent(context, VideoActivity::class.java).apply {
                putExtra(VIDEO, image)
            }

    }

    private val viewModel: VideoViewModel by viewModels { viewModelFactory }
    private lateinit var video: Image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        intent.getSerializableExtra(VIDEO)?.also {
            if (savedInstanceState == null) {
                video = it as Image
                viewModel.initMedia(video)
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
            video.observe(this@VideoActivity) {
                gallery_video.setVideoPath(it.path)
                gallery_video.start()
            }
            delete.observe(this@VideoActivity) {
                deleteAnimation()
            }
            toGallery.observe(this@VideoActivity) {
                toGalleryAnimation()
            }
            openRename.observe(this@VideoActivity) {
                openRenameAnimation()
            }
            scanImage.observe(this@VideoActivity) {
                scanMedia(it)
            }
            finish.observe(this@VideoActivity) {
                finish()
            }
            imageError.observe(this@VideoActivity) {
                if (it == ImageErrorType.TO_GALLERY) {
                    showErrorToast(this@VideoActivity, getString(R.string.error_to_gallery))
                }
            }
        }
    }

    override fun onAnimationEnd() {
        viewModel.animationEnd()
    }

}
