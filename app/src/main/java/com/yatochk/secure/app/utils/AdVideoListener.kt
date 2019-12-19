package com.yatochk.secure.app.utils

import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener

abstract class AdVideoListener : RewardedVideoAdListener {
    override fun onRewarded(rewardItem: RewardItem) {
        //do nothing
    }

    override fun onRewardedVideoAdLoaded() {
        //do nothing
    }

    override fun onRewardedVideoAdOpened() {
        //do nothing
    }

    override fun onRewardedVideoStarted() {
        //do nothing
    }

    override fun onRewardedVideoAdFailedToLoad(i: Int) {
        //do nothing
    }

    override fun onRewardedVideoAdClosed() {
        onCancelDisplay()
    }

    override fun onRewardedVideoAdLeftApplication() {
        onCancelDisplay()
    }

    override fun onRewardedVideoCompleted() {
        onCancelDisplay()
    }

    abstract fun onCancelDisplay()
}