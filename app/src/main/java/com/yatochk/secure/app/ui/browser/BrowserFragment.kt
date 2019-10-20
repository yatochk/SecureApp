package com.yatochk.secure.app.ui.browser

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
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

    private lateinit var urlConstraint: ConstraintSet
    private lateinit var editUrlConstraint: ConstraintSet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        urlConstraint = ConstraintSet()
        urlConstraint.clone(root_browser)
        editUrlConstraint = ConstraintSet()
        editUrlConstraint.clone(activity, R.layout.fragment_browser_edit)
        web_view.webChromeClient = object : WebChromeClient() {

        }
        edit_url.onFocusChangeListener = View.OnFocusChangeListener { _, isFocuse ->
            viewModel.editFocused(isFocuse)
        }
        edit_url.onSearch {
            viewModel.inputUrl(it)
        }
        observers()
    }

    private fun animateEditUrl(showNew: Boolean) {
        TransitionManager.beginDelayedTransition(root_browser)
        (if (showNew) editUrlConstraint else urlConstraint).applyTo(root_browser)
    }

    private fun observers() {
        viewModel.loadUrl.observe(this) {
            web_view.loadUrl(it)
        }
        viewModel.showUrl.observe(this) {
            edit_url.setText(it)
        }
        viewModel.editMode.observe(this) {
            animateEditUrl(it)
        }
    }
}