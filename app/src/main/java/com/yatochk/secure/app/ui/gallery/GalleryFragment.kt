package com.yatochk.secure.app.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexboxLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.utils.observe
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_gallery

    private val viewModel: GalleryViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: AlbumRecyclerAdapter

    override fun inject() {
        SecureApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AlbumRecyclerAdapter()
        recycler_gallery.layoutManager = FlexboxLayoutManager(activity!!)
        recycler_gallery.adapter = adapter
        observers()
    }

    private fun observers() {
        viewModel.albums.observe(this) {
            adapter.submitList(it)
        }
    }

}