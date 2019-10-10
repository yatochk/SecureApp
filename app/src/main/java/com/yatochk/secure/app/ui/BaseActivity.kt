package com.yatochk.secure.app.ui

import android.media.MediaScannerConnection
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yatochk.secure.app.dagger.ViewModelFactory
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    fun scanMedia(path: String) {
        MediaScannerConnection.scanFile(
            this,
            arrayOf(path),
            null,
            null
        )
    }

}