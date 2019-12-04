package com.yatochk.secure.app.dagger.components

import com.yatochk.secure.app.dagger.ViewModelFactory
import com.yatochk.secure.app.dagger.modules.AppModule
import com.yatochk.secure.app.dagger.modules.ViewModelModule
import com.yatochk.secure.app.ui.albums.AlbumActivity
import com.yatochk.secure.app.ui.browser.BrowserFragment
import com.yatochk.secure.app.ui.calculator.CalculatorActivity
import com.yatochk.secure.app.ui.contact.ContactActivity
import com.yatochk.secure.app.ui.contact.ContactFragment
import com.yatochk.secure.app.ui.gallery.GalleryFragment
import com.yatochk.secure.app.ui.image.ImageActivity
import com.yatochk.secure.app.ui.main.MainActivity
import com.yatochk.secure.app.ui.notes.NoteActivity
import com.yatochk.secure.app.ui.notes.NotesFragment
import com.yatochk.secure.app.ui.video.VideoActivity
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

    fun inject(calculatorActivity: CalculatorActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(albumActivity: AlbumActivity)
    fun inject(imageActivity: ImageActivity)
    fun inject(contactActivity: ContactActivity)
    fun inject(videoActivity: VideoActivity)
    fun inject(noteActivity: NoteActivity)
    fun inject(viewModelFactory: ViewModelFactory)
    fun inject(galleryFragment: GalleryFragment)
    fun inject(contactFragment: ContactFragment)
    fun inject(notesFragment: NotesFragment)
    fun inject(browserFragment: BrowserFragment)

}