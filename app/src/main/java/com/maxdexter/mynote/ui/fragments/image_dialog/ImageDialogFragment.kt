package com.maxdexter.mynote.ui.fragments.image_dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.R

class ImageDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = ImageDialogFragment()
    }

    private lateinit var viewModel: ImageDialogViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(ImageDialogViewModel::class.java)


        return inflater.inflate(R.layout.image_dialog_fragment, container, false)
    }



}