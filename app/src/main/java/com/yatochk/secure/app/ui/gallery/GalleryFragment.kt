package com.yatochk.secure.app.ui.gallery

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.yatochk.secure.app.R
import com.yatochk.secure.app.dagger.SecureApplication
import com.yatochk.secure.app.ui.BaseFragment
import com.yatochk.secure.app.ui.albums.AlbumActivity
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
        adapter = AlbumRecyclerAdapter { album, textView ->
            viewModel.clickAlbum(album, textView)
        }
        recycler_gallery.layoutManager = GridLayoutManager(activity!!, 2)
        recycler_gallery.adapter = adapter
        observers()
    }

    private fun observers() {
        with(viewModel) {
            albums.observe(this@GalleryFragment) {
                adapter.submitList(it)
            }

            openAlbum.observe(this@GalleryFragment) {
                val bundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions.makeSceneTransitionAnimation(
                        activity,
                        it.second,
                        getString(R.string.album_transition)
                    ).toBundle()
                } else {
                    null
                }
                startActivity(AlbumActivity.intent(activity!!, it.first), bundle)
            }
        }
    }

}