package com.yatochk.secure.app.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.hadilq.liveevent.LiveEvent

inline fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, crossinline body: (T) -> Unit) =
    this.observe(lifecycleOwner, Observer {
        body(it)
    })

fun LiveEvent<Void>.postEvent() {
    value = null
}