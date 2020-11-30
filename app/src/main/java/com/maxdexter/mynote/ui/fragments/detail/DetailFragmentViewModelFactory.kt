package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailFragmentViewModelFactory(private val uuid: String, private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailFragmentViewModel::class.java)) {
            return DetailFragmentViewModel(uuid , context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}