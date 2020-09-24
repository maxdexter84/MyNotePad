package com.maxdexter.mynote.ui.fragments.simple

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.ui.fragments.password.PasswordNotesViewModel

class SimpleNoteViwModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimpleNoteViewModel::class.java)) {
            return SimpleNoteViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}