package com.yatochk.secure.app.dagger

import android.app.Application
import com.yatochk.secure.app.dagger.components.AppComponent
import com.yatochk.secure.app.dagger.components.DaggerAppComponent

class SecureApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }

}