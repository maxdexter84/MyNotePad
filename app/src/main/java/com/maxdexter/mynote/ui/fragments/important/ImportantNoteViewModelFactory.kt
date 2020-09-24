package com.maxdexter.mynote.ui.fragments.important

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.ui.fragments.detail.DetailFragmentViewModel

class ImportantNoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImportantNoteViewModel::class.java)) {
            return ImportantNoteViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}