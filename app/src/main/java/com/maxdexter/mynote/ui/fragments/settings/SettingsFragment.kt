package com.maxdexter.mynote.ui.fragments.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.SettingsFragmentBinding


class SettingsFragment : Fragment() {
  private lateinit var binding: SettingsFragmentBinding
    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewModel.toFireStore.observe(viewLifecycleOwner, { it ->
            if (it){
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToFireStoreFragment())
            }
     })



        return binding.root
    }



}