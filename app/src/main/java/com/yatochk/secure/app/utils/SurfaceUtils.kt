package com.yatochk.secure.app.utils

import android.view.SurfaceHolder

open class SurfaceHolderCallback : SurfaceHolder.Callback {
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        //default nothing
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        //default nothing
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        //default nothing
    }
}