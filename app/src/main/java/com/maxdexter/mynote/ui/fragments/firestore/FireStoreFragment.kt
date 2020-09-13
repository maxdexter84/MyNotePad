package com.maxdexter.mynote.ui.fragments.firestore


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.FireStoreFragmentBinding

class FireStoreFragment : Fragment() {

    companion object {
        fun newInstance() = FireStoreFragment()
    }
    private lateinit var binding: FireStoreFragmentBinding
    private lateinit var viewModel: FireStoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fire_store_fragment, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(FireStoreViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }



}