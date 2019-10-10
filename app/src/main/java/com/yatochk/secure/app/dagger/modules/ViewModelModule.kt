package com.yatochk.secure.app.dagger.modules

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.dagger.ViewModelKey
import com.yatochk.secure.app.ui.albums.AlbumViewModel
import com.yatochk.secure.app.ui.browser.BrowserViewModel
import com.yatochk.secure.app.ui.contact.ContactMenuViewModel
import com.yatochk.secure.app.ui.contact.ContactViewModel
import com.yatochk.secure.app.ui.gallery.GalleryMenuViewModel
import com.yatochk.secure.app.ui.gallery.GalleryViewModel
import com.yatochk.secure.app.ui.image.ImageVIewModel
import com.yatochk.secure.app.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    internal abstract fun homeViewModel(viewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BrowserViewModel::class)
    internal abstract fun browserViewModel(viewModel: BrowserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumViewModel::class)
    internal abstract fun albumViewModel(viewModel: AlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageVIewModel::class)
    internal abstract fun imageViewModel(viewModel: ImageVIewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    internal abstract fun contactViewModel(viewModel: ContactViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactMenuViewModel::class)
    internal abstract fun contactMenuViewModel(viewModel: ContactMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryMenuViewModel::class)
    internal abstract fun galleryMenuViewModel(viewModel: GalleryMenuViewModel): ViewModel

}