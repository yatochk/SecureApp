package com.yatochk.secure.app.utils

import com.google.android.gms.ads.AdRequest

fun getDefaultAdRequest(): AdRequest =
    AdRequest.Builder().addTestDevice(TEST_DEVICE).build()
