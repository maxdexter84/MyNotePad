package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class NoteListFragmentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteListFragmentViewModel::class.java)) {
            return NoteListFragmentViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}