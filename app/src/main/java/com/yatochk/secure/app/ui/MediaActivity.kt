package com.yatochk.secure.app.ui

import kotlinx.android.synthetic.main.activity_image.*

abstract class MediaActivity : BaseActivity() {

    companion object {
        private const val DURATION_ANIMATION = 500L
    }

    abstract fun onAnimationEnd()

    protected fun openRenameAnimation() {

    }

    protected fun toGalleryAnimation() {
        gallery_image.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .scaleX(0f)
            .scaleY(0f)
            .withEndAction {
                onAnimationEnd()
            }
            .start()

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
        gallery_image.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .scaleX(0f)
            .scaleY(0f)
            .withEndAction {
                onAnimationEnd()
            }
            .start()

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