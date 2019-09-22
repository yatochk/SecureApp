package com.yatochk.secure.app.dagger.modules

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.dagger.ViewModelKey
import com.yatochk.secure.app.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel

}