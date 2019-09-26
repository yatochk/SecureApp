package com.yatochk.secure.app.dagger.modules

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.dagger.ViewModelKey
import com.yatochk.secure.app.ui.browser.BrowserViewModel
import com.yatochk.secure.app.ui.home.GalleryViewModel
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

}