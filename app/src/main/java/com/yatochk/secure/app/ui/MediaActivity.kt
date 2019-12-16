package com.yatochk.secure.app.ui

import android.view.View
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.media_bar.*

abstract class MediaActivity : BaseActivity() {

    companion object {
        private const val DURATION_ANIMATION = 500L
    }

    abstract fun onAnimationEnd()
    abstract val mediaView: View?

    protected fun openRenameAnimation() {

    }

    protected fun toGalleryAnimation() {
        mediaView?.apply {
            animate()
                .alpha(0f)
                .setDuration(DURATION_ANIMATION)
                .scaleX(0f)
                .scaleY(0f)
                .withEndAction {
                    onAnimationEnd()
                }
                .start()
        }

        image_to_gallery.animate()
            .alpha(1f)
            .setDuration(DURATION_ANIMATION)
            .start()

        container_image_options.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .start()
    }

    protected fun deleteAnimation() {
        mediaView?.apply {
            animate()
                .alpha(0f)
                .setDuration(DURATION_ANIMATION)
                .scaleX(0f)
                .scaleY(0f)
                .withEndAction {
                    onAnimationEnd()
                }
                .start()
        }

        image_deleted.animate()
            .alpha(1f)
            .setDuration(DURATION_ANIMATION)
            .start()

        container_image_options.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .start()
    }
}