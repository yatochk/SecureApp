package com.yatochk.secure.app.ui.browser

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import androidx.fragment.app.viewModels
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.observe
import com.yatochk.secure.app.utils.onSearch
import kotlinx.android.synthetic.main.fragment_browser.*

class BrowserFragment : BaseFragment() {

    override val layoutId = R.layout.fragment_browser

    private val viewModel: BrowserViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        web_view.webChromeClient = object : WebChromeClient() {

        }
        edit_url.onSearch {
            viewModel.inputUrl(it)
        }
        observers()
    }

    private fun observers() {
        viewModel.loadUrl.observe(this) {
            web_view.loadUrl(it)
        }
        viewModel.showUrl.observe(this) {
            edit_url.setText(it)
        }
    }
}