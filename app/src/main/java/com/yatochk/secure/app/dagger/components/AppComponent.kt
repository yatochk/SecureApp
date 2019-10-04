package com.yatochk.secure.app.dagger.components

import com.yatochk.secure.app.dagger.ViewModelFactory
import com.yatochk.secure.app.dagger.modules.AppModule
import com.yatochk.secure.app.dagger.modules.ViewModelModule
import com.yatochk.secure.app.ui.albums.AlbumActivity
import com.yatochk.secure.app.ui.browser.BrowserFragment
import com.yatochk.secure.app.ui.gallery.GalleryFragment
import com.yatochk.secure.app.ui.image.ImageActivity
import com.yatochk.secure.app.ui.main.MainActivity
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

    fun inject(mainActivity: MainActivity)
    fun inject(albumActivity: AlbumActivity)
    fun inject(imageActivity: ImageActivity)
    fun inject(viewModelFactory: ViewModelFactory)
    fun inject(browserFragment: BrowserFragment)
    fun inject(galleryFragment: GalleryFragment)

}