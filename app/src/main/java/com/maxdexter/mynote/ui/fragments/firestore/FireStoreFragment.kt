package com.maxdexter.mynote.ui.fragments.firestore

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdexter.mynote.R

class FireStoreFragment : Fragment() {

    companion object {
        fun newInstance() = FireStoreFragment()
    }

    private lateinit var viewModel: FireStoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fire_store_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)
        // TODO: Use the ViewModel
    }

}