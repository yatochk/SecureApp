package com.yatochk.secure.app.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yatochk.secure.app.R
import kotlinx.android.synthetic.main.dialog_new_album.view.*

class NewAlbumDialog : DialogFragment() {

    var createListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.dialog_new_album, container).apply {
            btn_create.setOnClickListener {
                createListener?.invoke(edit_new_album.text.toString())
            }
        }

}