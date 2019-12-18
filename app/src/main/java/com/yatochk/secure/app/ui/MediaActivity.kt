package com.yatochk.secure.app.ui

import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.yatochk.secure.app.ui.image.NewAlbumDialog
import kotlinx.android.synthetic.main.activity_image.*

abstract class MediaActivity : BaseActivity() {

    companion object {
        private const val DURATION_ANIMATION = 500L
        private const val NEW_ALBUM = "new_album"
    }

    abstract fun onAnimationEnd()
    abstract val mediaView: View?
    abstract val globalContainer: ConstraintLayout
    abstract val openedAlbumPicker: ConstraintSet
    abstract val closedAlbumPicker: ConstraintSet

    protected val albumDialog = NewAlbumDialog()

    protected fun animateAlbumPicker(needOpen: Boolean) {
        TransitionManager.beginDelayedTransition(globalContainer)
        (if (needOpen) openedAlbumPicker else closedAlbumPicker).applyTo(globalContainer)
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

        container_media_options.animate()
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

        container_media_options.animate()
            .alpha(0f)
            .setDuration(DURATION_ANIMATION)
            .start()
    }

    protected fun openNewAlbumDialog() {
        if (supportFragmentManager.findFragmentByTag(NEW_ALBUM) == null) {
            albumDialog.show(supportFragmentManager, NEW_ALBUM)
        }
    }
}