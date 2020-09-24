package com.maxdexter.mynote.ui.fragments.password


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
import com.maxdexter.mynote.databinding.PasswordNotesFragmentBinding
import com.maxdexter.mynote.ui.fragments.simple.SimpleNoteFragmentDirections
import com.maxdexter.mynote.ui.fragments.simple.SimpleNoteViewModel
import com.maxdexter.mynote.ui.fragments.simple.SimpleNoteViwModelFactory

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
       initViewModel()
        initRecyclerAdapter()




        return binding.root
    }

    private fun initRecyclerAdapter() {
        binding.passwordListRecycler.layoutManager = LinearLayoutManager(context)
        val noteAdapter = NoteAdapter(NoteListener { findNavController().navigate(PasswordNotesFragmentDirections.actionPasswordNotesFragmentToDetailFragment(it)) })
        viewModel.passwordNote.observe(viewLifecycleOwner, { it.let { noteAdapter.submitList(it) } })
        binding.passwordListRecycler.adapter = noteAdapter
    }

    private fun initViewModel() {
        val context = context
        if (context != null)
            viewModelFactory = PasswordNotesViewModelFactory(context)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PasswordNotesViewModel::class.java)
    }



}