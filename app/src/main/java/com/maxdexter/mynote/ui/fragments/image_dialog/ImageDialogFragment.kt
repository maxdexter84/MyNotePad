package com.maxdexter.mynote.ui.fragments.image_dialog


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ImageDialogFragmentBinding
import com.maxdexter.mynote.extensions.setImage

class ImageDialogFragment :BottomSheetDialogFragment() {
    lateinit var binding: ImageDialogFragmentBinding

    private lateinit var viewModel: ImageDialogViewModel
    private lateinit var viewModelFactory: ImageDialogViewModelFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.image_dialog_fragment, container, false)
        val args = arguments?.let { ImageDialogFragmentArgs.fromBundle(it) }?.path
        if (args != null)
        viewModelFactory = ImageDialogViewModelFragment(args)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageDialogViewModel::class.java)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.ivImageDialogFragment.setImage(requireContext(), args)

        return binding.root
    }



}