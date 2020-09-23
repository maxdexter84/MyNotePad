package com.maxdexter.mynote.ui.fragments.password


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.PasswordNotesFragmentBinding

class PasswordNotesFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordNotesFragment()
    }

    private lateinit var viewModel: PasswordNotesViewModel
    private lateinit var viewModelFactory: PasswordNotesViewModelFactory
    private lateinit var binding: PasswordNotesFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.password_notes_fragment, container, false)
        val args = arguments?.let { PasswordNotesFragmentArgs.fromBundle(it) }
        val context = context
        if (args != null && context != null) {
            viewModelFactory = PasswordNotesViewModelFactory(args.noteType, context)
            viewModel = ViewModelProvider(this, viewModelFactory).get(PasswordNotesViewModel::class.java)
        }




        return binding.root
    }



}