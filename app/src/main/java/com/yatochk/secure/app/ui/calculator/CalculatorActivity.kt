package com.yatochk.secure.app.ui.calculator

import android.os.Bundle
import androidx.activity.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.ui.main.MainActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : BaseActivity() {

    private val viewModel: CalculatorViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        keyboard.setKeysLongListener {
            viewModel.inputKey(it)
        }
        keyboard.setKeysListener {
            viewModel.inputKey(it)
        }
        observers()
    }

    private fun observers() {
        with(viewModel) {
            displayResult.observe(this@CalculatorActivity) {
                text_result.text = it
            }
            openContent.observe(this@CalculatorActivity) {
                startActivity(MainActivity.intent(this@CalculatorActivity))
            }
        }
    }

}