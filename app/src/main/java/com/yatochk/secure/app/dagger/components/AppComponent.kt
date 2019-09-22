package com.yatochk.secure.app.dagger.components

import com.yatochk.secure.app.MainActivity
import com.yatochk.secure.app.dagger.modules.AppModule
import com.yatochk.secure.app.dagger.modules.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun injectActivity(mainActivity: MainActivity)

}