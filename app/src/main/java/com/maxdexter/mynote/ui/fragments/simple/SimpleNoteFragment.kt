package com.maxdexter.mynote.ui.fragments.simple

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdexter.mynote.R

class SimpleNoteFragment : Fragment() {

    companion object {
        fun newInstance() = SimpleNoteFragment()
    }

    private lateinit var viewModel: SimpleNoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SimpleNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}