package com.yatochk.secure.app.dagger

import android.app.Application
import com.yatochk.secure.app.dagger.components.AppComponent
import com.yatochk.secure.app.dagger.components.DaggerAppComponent
import com.yatochk.secure.app.dagger.modules.AppModule

class SecureApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}