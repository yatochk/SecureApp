package com.yatochk.secure.app.ui.albums

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity

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

}