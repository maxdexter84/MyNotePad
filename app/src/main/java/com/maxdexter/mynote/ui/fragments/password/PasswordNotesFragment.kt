package com.maxdexter.mynote.ui.fragments.password

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdexter.mynote.R

class PasswordNotesFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordNotesFragment()
    }

    private lateinit var viewModel: PasswordNotesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.password_notes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PasswordNotesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}