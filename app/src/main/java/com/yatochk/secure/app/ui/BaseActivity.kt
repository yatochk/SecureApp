package com.yatochk.secure.app.ui

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.yatochk.secure.app.dagger.ViewModelFactory
import java.io.File
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val adViewModel: AdViewModel by viewModels { viewModelFactory }

    protected abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        adViewModel.onScreenOpened()
    }

    fun scanMedia(path: String) {
        MediaScannerConnection.scanFile(
            this,
            arrayOf(path),
            null
        ) { createdPath, uri ->
            FirebaseAnalytics.getInstance(this).logEvent(
                "Scan_media_finished",
                Bundle().apply {
                    putString("Path", createdPath)
                    putString("Uri", uri.toString())
                }
            )
        }
    }

    /**
     * <p> sending broadcast to media scanner and call MediaScannerConnection with path of file
     * @param file for scan
     * @see MediaScannerConnection.scanFile()
     */
    fun scanMedia(file: File) {
        sendBroadcast(
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(file)
            }
        )
        scanMedia(file.absolutePath)
        scanMedia(file.path)
    }

}