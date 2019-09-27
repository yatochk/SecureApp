package com.yatochk.secure.app.dagger.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.yatochk.secure.app.model.database.SecureDatabase
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.model.database.dao.NotesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val app: Application) {

    @Provides
    fun provideContext(): Context {
        return app
    }

    @Singleton
    @Provides
    fun provideMedicationDatabase(context: Context): SecureDatabase =
        Room.databaseBuilder(
            context,
            SecureDatabase::class.java,
            SecureDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideContactDao(database: SecureDatabase): ContactDao =
        database.contactDao

    @Singleton
    @Provides
    fun provideNotesDao(database: SecureDatabase): NotesDao =
        database.notesDao

}