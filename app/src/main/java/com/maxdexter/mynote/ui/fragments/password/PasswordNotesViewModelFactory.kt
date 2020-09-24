package com.maxdexter.mynote.ui.fragments.password

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.ui.fragments.important.ImportantNoteViewModel

class PasswordNotesViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordNotesViewModel::class.java)) {
            return PasswordNotesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}