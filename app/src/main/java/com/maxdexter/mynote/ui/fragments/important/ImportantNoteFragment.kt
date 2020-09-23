package com.maxdexter.mynote.ui.fragments.important

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ImportantNoteFragmentBinding
import io.grpc.Context

class ImportantNoteFragment : Fragment() {

    companion object {
        fun newInstance() = ImportantNoteFragment()
    }
    private lateinit var binding: ImportantNoteFragmentBinding
    private lateinit var viewModelFactory: ImportantNoteViewModelFactory
    private lateinit var viewModel: ImportantNoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.important_note_fragment, container, false)
        val args = arguments?.let { ImportantNoteFragmentArgs.fromBundle(it) }
        val context = context
        if (args != null && context != null) {
            viewModelFactory = ImportantNoteViewModelFactory(args.noteType, context)

        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImportantNoteViewModel::class.java)

        return binding.root
    }





}