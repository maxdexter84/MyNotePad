package com.maxdexter.mynote.ui.fragments.simple


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
import com.maxdexter.mynote.databinding.SimpleNoteFragmentBinding

class SimpleNoteFragment : Fragment() {
    private lateinit var binding: SimpleNoteFragmentBinding
    private lateinit var viewModel: SimpleNoteViewModel
    private lateinit var viewModelFactory: SimpleNoteViwModelFactory

    companion object {
        fun newInstance() = SimpleNoteFragment()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.simple_note_fragment, container, false)
        initViewModel()
        initRecyclerAdapter()

        return binding.root
    }

    private fun initRecyclerAdapter() {
        binding.simpleNoteRecycler.layoutManager = LinearLayoutManager(context)
        val noteAdapter = NoteAdapter(NoteListener { findNavController().navigate(SimpleNoteFragmentDirections.actionSimpleNoteFragmentToDetailFragment(it)) })
        viewModel.allNoteList.observe(viewLifecycleOwner, { it.let { noteAdapter.submitList(it) } })
        binding.simpleNoteRecycler.adapter = noteAdapter
    }

    private fun initViewModel() {
        val context = context
        if (context != null)
            viewModelFactory = SimpleNoteViwModelFactory(context)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SimpleNoteViewModel::class.java)
    }


}