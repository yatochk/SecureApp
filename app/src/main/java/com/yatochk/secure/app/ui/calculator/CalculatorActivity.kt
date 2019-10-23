package com.yatochk.secure.app.ui.calculator

import android.os.Bundle
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : BaseActivity() {

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        keyboard.displayView = text_result
        keyboard.setKeysListener {

        }
    }

}