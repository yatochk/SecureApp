package com.yatochk.secure.app.ui.albums

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.gallery.ImageRecyclerAdapter
import com.yatochk.secure.app.ui.image.ImageActivity
import com.yatochk.secure.app.utils.observe
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
        adapter = ImageRecyclerAdapter { path, imageView ->
            viewModel.clickImage(path, imageView)
        }
        recycler_album_photo.layoutManager = GridLayoutManager(this, 4)
        recycler_album_photo.adapter = adapter
        intent.getStringExtra(ALBUM_NAME)!!.also {
            text_album_name.text = it
            if (savedInstanceState == null) {
                viewModel.initAlbum(it)
            }
        }
        Handler().postDelayed({
            observers()
        }, 500) //TODO
    }

    private fun observers() {
        with(viewModel) {
            images.observe(this@AlbumActivity) {
                adapter.submitList(it)
            }
            openImage.observe(this@AlbumActivity) {
                val bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions.makeSceneTransitionAnimation(
                        this@AlbumActivity,
                        it.second,
                        getString(R.string.album_transition)
                    ).toBundle()
                } else {
                    null
                }
                startActivity(ImageActivity.intent(this@AlbumActivity, it.first), bundle)
            }
            showError.observe(this@AlbumActivity) {
                //TODO
            }
        }
    }

}