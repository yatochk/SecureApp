package com.yatochk.secure.app.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.getbase.floatingactionbutton.FloatingActionButton
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseActivity
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_gallery,
                R.id.navigation_contact,
                R.id.navigation_notes,
                R.id.navigation_internet
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        galleryFloatingMenu()
        nav_view.setOnNavigationItemSelectedListener {
            floating_menu.isVisible = it.itemId != R.id.navigation_internet
            when (it.itemId) {
                R.id.navigation_gallery -> {
                    galleryFloatingMenu()
                    true
                }
                R.id.navigation_contact -> {
                    true
                }
                R.id.navigation_notes -> {
                    true
                }
                R.id.navigation_internet -> {
                    true
                }
                else -> throw IllegalStateException("Not implement FAButton for ${it.itemId}")
            }
        }
        initObservers()
    }

    private fun galleryFloatingMenu() {
        val photoButton = FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_camera)
            setOnClickListener {
                viewModel.clickPhoto()
            }
        }
        val galleryButton = FloatingActionButton(this).apply {
            setIcon(R.drawable.ic_gallery_locale)
            setOnClickListener {
                viewModel.clickGallery()
            }
        }
        floating_menu.addButton(galleryButton)
        floating_menu.addButton(photoButton)
    }

    private fun initObservers() {
        viewModel.openCamera.observe(this) {

        }
        viewModel.openGallery.observe(this) {

        }
    }

}
