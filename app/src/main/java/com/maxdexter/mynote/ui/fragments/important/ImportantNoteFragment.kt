package com.maxdexter.mynote.ui.fragments.important

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.adapters.NoteAdapter
import com.maxdexter.mynote.data.adapters.NoteListener
import com.maxdexter.mynote.databinding.ImportantNoteFragmentBinding
import com.maxdexter.mynote.ui.fragments.password.PasswordNotesFragmentDirections
import com.maxdexter.mynote.ui.fragments.password.PasswordNotesViewModel
import com.maxdexter.mynote.ui.fragments.password.PasswordNotesViewModelFactory
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

        initViewModel()
        initRecyclerAdapter()

        return binding.root
    }


    private fun initRecyclerAdapter() {
        binding.importantNoteRecycler.layoutManager = LinearLayoutManager(context)
        val noteAdapter = NoteAdapter(NoteListener { findNavController().navigate(ImportantNoteFragmentDirections.actionImportantNoteFragmentToDetailFragment(it)) })
        viewModel.importantNote.observe(viewLifecycleOwner, { it.let { noteAdapter.submitList(it) } })
        binding.importantNoteRecycler.adapter = noteAdapter
    }

    private fun initViewModel() {
        val context = context
        if (context != null)
            viewModelFactory = ImportantNoteViewModelFactory(context)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImportantNoteViewModel::class.java)
    }


}