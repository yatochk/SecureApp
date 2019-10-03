package com.yatochk.secure.app.ui.albums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
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

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        text_album_name.text = intent.getStringExtra(ALBUM_NAME)
    }

}