package com.maxdexter.mynote.ui.fragments.settings

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.repository.Repository


class SettingsViewModelFactory(private val repository: Repository?, private val owner: LifecycleOwner, private val context: Context?) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository, owner, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}