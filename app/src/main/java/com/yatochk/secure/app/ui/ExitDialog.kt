package com.yatochk.secure.app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.yatochk.secure.app.R
import com.yatochk.secure.app.utils.getDefaultAdRequest
import kotlinx.android.synthetic.main.dialog_exit.*
import kotlinx.android.synthetic.main.dialog_exit.view.*

class ExitDialog : Fragment() {

    var cancelListener: (() -> Unit)? = null
    var exitListener: (() -> Unit)? = null

    private var adView: AdView? = null
    private val adsParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.FILL_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT,
        android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL
    )

    fun initAds(context: Context) {
        adView = AdView(context).apply {
            adSize = AdSize.MEDIUM_RECTANGLE
            adUnitId = context.getString(R.string.exit_ad)
            loadAd(getDefaultAdRequest())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.dialog_exit, container, false).apply {
            cancel_btn.setOnClickListener {
                cancelListener?.invoke()
            }
            exit_btn.setOnClickListener {
                exitListener?.invoke()
            }
            adView?.also {
                if (it.parent == null && ad_frame.childCount == 0) {
                    ad_frame.addView(it, adsParams)
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        ad_frame.removeView(adView)
    }

}