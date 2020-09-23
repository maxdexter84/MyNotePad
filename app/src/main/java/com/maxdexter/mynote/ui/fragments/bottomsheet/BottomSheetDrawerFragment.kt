package com.maxdexter.mynote.ui.fragments.bottomsheet

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.BottomSheetDrawerFragmentBinding
import com.maxdexter.mynote.ui.fragments.important.ImportantNoteFragment

class BottomSheetDrawerFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = BottomSheetDrawerFragment()
    }

    private lateinit var viewModel: BottomSheetDrawerViewModel
    private lateinit var binding: BottomSheetDrawerFragmentBinding
    private lateinit var navigationView: NavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_drawer_fragment, container, false)

       val mNavHostFragment = parentFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        val navController = mNavHostFragment?.navController
        navigationView = binding.navigationView
        viewModel = ViewModelProvider(this).get(BottomSheetDrawerViewModel::class.java)
        if (navController != null) {
            navigationView.setupWithNavController(navController)
        }

//        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
//            when(menuItem.itemId) {
//                R.id.simpleNoteFragment -> findNavController().navigate(BottomSheetDrawerFragmentDirections.actionBottomSheetDrawerFragmentToSimpleNoteFragment())
//                R.id.passwordNotesFragment -> findNavController().navigate(BottomSheetDrawerFragmentDirections.actionBottomSheetDrawerFragmentToPasswordNotesFragment())
//                R.id.importantNoteFragment -> {
//                    findNavController().navigate(BottomSheetDrawerFragmentDirections.actionBottomSheetDrawerFragmentToImportantNoteFragment())
//
//                }
//            }
//            true
//        }
        return binding.root
    }



}