package com.yatochk.secure.app.utils

import android.animation.Animator
import android.animation.ObjectAnimator

inline fun ObjectAnimator.setCompleteListener(crossinline listener: () -> Unit): ObjectAnimator {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {

        }

        override fun onAnimationEnd(p0: Animator?) {
            listener()
        }

        override fun onAnimationCancel(p0: Animator?) {

        }

        override fun onAnimationStart(p0: Animator?) {

        }

    })
    return this
}