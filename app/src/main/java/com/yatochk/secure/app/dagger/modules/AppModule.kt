package com.yatochk.secure.app.dagger.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.yatochk.secure.app.R
import com.yatochk.secure.app.model.database.SecureDatabase
import com.yatochk.secure.app.model.database.dao.ContactDao
import com.yatochk.secure.app.model.database.dao.ImagesDao
import com.yatochk.secure.app.model.database.dao.NotesDao
import com.yatochk.secure.app.utils.AdVideoListener
import com.yatochk.secure.app.utils.getDefaultAdRequest
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
    fun provideSharedPreerences(): SharedPreferences =
        app.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

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

    @Singleton
    @Provides
    fun provideImagesDao(database: SecureDatabase): ImagesDao =
        database.imagesDao

    @Singleton
    @Provides
    fun provideInterstitialAd() =
        InterstitialAd(app).apply {
            MobileAds.initialize(app, app.getString(R.string.admob_app_id))
            adUnitId = app.getString(R.string.interstitial_ad)
            loadAd(getDefaultAdRequest())
            setRewardedVideoAdListener(object : AdVideoListener() {
                override fun onCancelDisplay() {
                    loadAd(getDefaultAdRequest())
                }
            })
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    Log.i("InterstitialAd", "load failed with code: $p0")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.i("InterstitialAd", "load success")
                }
            }
        }

}