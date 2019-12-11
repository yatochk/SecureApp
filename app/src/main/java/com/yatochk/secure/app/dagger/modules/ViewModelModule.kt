package com.yatochk.secure.app.dagger.modules

import androidx.lifecycle.ViewModel
import com.yatochk.secure.app.MediaViewModel
import com.yatochk.secure.app.dagger.ViewModelKey
import com.yatochk.secure.app.ui.albums.AlbumViewModel
import com.yatochk.secure.app.ui.browser.BrowserViewModel
import com.yatochk.secure.app.ui.calculator.CalculatorViewModel
import com.yatochk.secure.app.ui.contact.ContactMenuViewModel
import com.yatochk.secure.app.ui.contact.ContactViewModel
import com.yatochk.secure.app.ui.contact.EditContactViewModel
import com.yatochk.secure.app.ui.gallery.GalleryMenuViewModel
import com.yatochk.secure.app.ui.gallery.GalleryViewModel
import com.yatochk.secure.app.ui.image.ImageViewModel
import com.yatochk.secure.app.ui.main.MainViewModel
import com.yatochk.secure.app.ui.notes.EditNoteViewModel
import com.yatochk.secure.app.ui.notes.NotesViewModel
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
    @ViewModelKey(ImageViewModel::class)
    internal abstract fun imageViewModel(viewModel: ImageViewModel): ViewModel

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
    @ViewModelKey(EditContactViewModel::class)
    internal abstract fun editContactViewModel(viewModel: EditContactViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryMenuViewModel::class)
    internal abstract fun galleryMenuViewModel(viewModel: GalleryMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    internal abstract fun notesViewModel(viewModel: NotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditNoteViewModel::class)
    internal abstract fun editNoteViewModel(viewModel: EditNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    internal abstract fun mediaViewModel(viewModel: MediaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalculatorViewModel::class)
    internal abstract fun calculatorViewModel(viewModel: CalculatorViewModel): ViewModel

}