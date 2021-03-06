package com.yatochk.secure.app.ui.albums

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.transition.addListener
import androidx.recyclerview.widget.GridLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.gallery.ImageRecyclerAdapter
import com.yatochk.secure.app.ui.image.ImageActivity
import com.yatochk.secure.app.ui.video.VideoActivity
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.setCompleteListener
import com.yatochk.secure.app.utils.showErrorToast
import kotlinx.android.synthetic.main.activity_album.*

class AlbumActivity : BaseActivity() {

    companion object {
        private const val ALBUM_NAME = "album_name"

        fun intent(context: Context, albumName: String) =
            Intent(context, AlbumActivity::class.java).apply {
                putExtra(ALBUM_NAME, albumName)
            }
    }

    private val viewModel: AlbumViewModel by viewModels { viewModelFactory }
    private lateinit var adapter: ImageRecyclerAdapter

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        adapter = ImageRecyclerAdapter { image, imageView ->
            viewModel.clickImage(image, imageView)
        }
        recycler_album_photo.layoutManager = GridLayoutManager(this, 4)
        recycler_album_photo.adapter = adapter
        intent.getStringExtra(ALBUM_NAME)!!.also {
            text_album_name.text = it
            if (savedInstanceState == null) {
                viewModel.initAlbum(it)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition.addListener(onEnd = {
                viewModel.screenOpened()
            })
        } else {
            viewModel.screenOpened()
        }
        viewModel.startObserving.observe(this) {
            startObservers()
        }
    }

    private fun startObservers() {
        with(viewModel) {
            images.observe(this@AlbumActivity) { imageList ->
                adapter.submitList(imageList)
            }
            openImage.observe(this@AlbumActivity) {
                val bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions.makeSceneTransitionAnimation(
                        this@AlbumActivity,
                        it.second,
                        getString(R.string.image_transition)
                    ).toBundle()
                } else {
                    null
                }
                startActivity(ImageActivity.intent(this@AlbumActivity, it.first), bundle)
            }
            openVideo.observe(this@AlbumActivity) {
                startActivity(VideoActivity.intent(this@AlbumActivity, it))
            }
            finish.observe(this@AlbumActivity) {
                finish()
            }
            showImageError.observe(this@AlbumActivity) {
                showErrorToast(this@AlbumActivity, it)
            }
            decryptionSeek.observe(this@AlbumActivity) {
                showDecryptingProgress(it)
            }
        }
    }

    private fun showDecryptingProgress(data: DecryptionSeek) {
        val progress = (100 * data.decryptedCount.toDouble() / data.count).toInt()
        ObjectAnimator.ofInt(progress_decrypt, "progress", progress)
            .setDuration(500)
            .setCompleteListener {
                if (!data.needShow) {
                    progress_decrypt.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .start()
                }
            }
            .start()
    }

}