package com.maxdexter.mynote.ui.fragments.image_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageDialogViewModelFragment(private val path: String): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageDialogViewModel::class.java)) {
            return ImageDialogViewModel(path) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}