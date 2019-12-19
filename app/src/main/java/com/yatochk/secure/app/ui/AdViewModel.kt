package com.yatochk.secure.app.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.InterstitialAd
import com.yatochk.secure.app.utils.COUNT_INTERSTITIAL
import javax.inject.Inject

class AdViewModel @Inject constructor(
    private val interstitialAd: InterstitialAd
) : ViewModel() {

    private var openCount = 0
        set(value) {
            if (openCount >= COUNT_INTERSTITIAL) {
                field = 0
                if (interstitialAd.isLoaded) {
                    interstitialAd.show()
                } else {
                    Log.i("InterstitialAd", "not displaying: ads not loaded")
                }
            } else {
                field = value
            }
        }

    fun onScreenOpened() {
        openCount++
    }

}