package com.yatochk.secure.app.ui.video

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.activity.viewModels
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.MediaActivity
import com.yatochk.secure.app.utils.SurfaceHolderCallback
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File
import java.io.FileInputStream


class VideoActivity : MediaActivity() {

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    companion object {
        private const val VIDEO = "opened_video"

        fun intent(context: Context, videoPath: String) =
            Intent(context, VideoActivity::class.java).apply {
                putExtra(VIDEO, videoPath)
            }

    }

    private val viewModel: MediaViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        intent.getStringExtra(VIDEO)?.also { path ->
            val fi = FileInputStream(File(path))
            val videoPlayer = MediaPlayer()
            videoPlayer.setDataSource(fi.fd)
            surface_view.holder.addCallback(object : SurfaceHolderCallback() {
                override fun surfaceCreated(p0: SurfaceHolder?) {
                    videoPlayer.setDisplay(p0)
                }
            })
            videoPlayer.prepare()
            videoPlayer.setOnPreparedListener {
                it.start()
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
            mediaError.observe(this@VideoActivity) {
                showErrorToast(this@VideoActivity, it)
            }
        }
    }

    override fun onAnimationEnd() {
        viewModel.animationEnd()
    }

    override val mediaView: View?
        get() = surface_view
}
