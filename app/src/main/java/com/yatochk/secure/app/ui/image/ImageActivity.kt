package com.yatochk.secure.app.ui.image

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.model.images.ImageSecureController
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject

class ImageActivity : BaseActivity() {

    companion object {

        private const val IMAGE_PATH = "opened_image"

        fun intent(context: Context, path: String) =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(IMAGE_PATH, path)
            }

    }

    private val viewModel: ImageVIewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var imageSecureController: ImageSecureController

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        intent.getStringExtra(IMAGE_PATH)?.also {
            if (savedInstanceState == null) {
                val decryptedBytes = imageSecureController.decryptImage(it)
                image_content.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        decryptedBytes,
                        0,
                        decryptedBytes.size
                    )
                )
            }
        }
        observers()
    }

    private fun observers() {
        with(viewModel) {
            image.observe(this@ImageActivity) {
            }
        }
    }

}