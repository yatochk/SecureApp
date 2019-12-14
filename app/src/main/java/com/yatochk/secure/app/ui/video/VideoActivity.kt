package com.yatochk.secure.app.ui.video

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.MediaController
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


class VideoActivity : MediaActivity(), MediaController.MediaPlayerControl {

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

    private val videoPlayer = MediaPlayer()
    private lateinit var videoController: MediaController
    private val viewModel: MediaViewModel by viewModels { viewModelFactory }

    private fun initPlayer(path: String) {
        videoController = MediaController(this)
        val fi = FileInputStream(File(path))
        videoPlayer.setDataSource(fi.fd)
        surface_view.holder.addCallback(object : SurfaceHolderCallback() {
            override fun surfaceCreated(p0: SurfaceHolder?) {
                videoPlayer.setDisplay(p0)
            }
        })
        videoPlayer.prepare()
        videoPlayer.setOnPreparedListener {
            videoController.setMediaPlayer(this)
            videoController.setAnchorView(surface_view)
            it.start()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        videoController.show()
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        intent.getStringExtra(VIDEO)?.also { path ->
            initPlayer(path)
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

    override fun onPause() {
        super.onPause()
        videoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.reset()
    }

    override fun onAnimationEnd() {
        viewModel.animationEnd()
    }

    override val mediaView: View?
        get() = surface_view

    override fun isPlaying(): Boolean = videoPlayer.isPlaying

    override fun canSeekForward(): Boolean = true

    override fun getDuration(): Int = videoPlayer.duration

    override fun pause() {
        videoPlayer.pause()
    }

    override fun getBufferPercentage(): Int = 0

    override fun seekTo(p0: Int) {
        videoPlayer.seekTo(p0)
    }

    override fun getCurrentPosition(): Int = videoPlayer.currentPosition

    override fun canSeekBackward(): Boolean = true

    override fun start() {
        videoPlayer.start()
    }

    override fun getAudioSessionId(): Int = videoPlayer.audioSessionId

    override fun canPause(): Boolean = true

}
