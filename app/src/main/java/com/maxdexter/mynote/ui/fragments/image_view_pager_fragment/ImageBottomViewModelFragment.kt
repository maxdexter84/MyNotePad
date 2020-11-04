package com.maxdexter.mynote.ui.fragments.image_view_pager_fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageBottomViewModelFragment(private val path: String, private val context: Context): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImageBottomViewModel::class.java)) {
            return ImageBottomViewModel(path, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}