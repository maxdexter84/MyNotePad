package com.maxdexter.mynote.ui.fragments.allnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.adapters.NoteAdapter
import com.maxdexter.mynote.data.adapters.NoteListener
import com.maxdexter.mynote.databinding.FragmentNoteListBinding



class NoteListFragment : Fragment() {
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var adapter: NoteAdapter
    //Обязательный интерфейс для активности хоста
    private lateinit var viewModel: NoteListFragmentViewModel
    private lateinit var viewModelFactory: NoteListFragmentViewModelFactory





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        initViewModel(inflater, container)
        initRecyclerAdapter()

        return binding.root
    }

    private fun initViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false)
        val args = arguments?.let { NoteListFragmentArgs.fromBundle(it) }
        val context = context
        if (context != null) {
            if (args != null) {
                viewModelFactory = NoteListFragmentViewModelFactory(args.noteType, context)
            }
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteListFragmentViewModel::class.java)
    }


    private fun initRecyclerAdapter() {
        binding.noteListId.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        adapter = NoteAdapter(NoteListener { findNavController().navigate(NoteListFragmentDirections.actionNoteListFragmentToDetailFragment(it)) })
        viewModel.allNoteList.observe(viewLifecycleOwner, { it.let { adapter.submitList(it) } })
        binding.noteListId.adapter = adapter
        }




}