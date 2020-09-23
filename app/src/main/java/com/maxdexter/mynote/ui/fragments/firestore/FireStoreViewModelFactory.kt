package com.maxdexter.mynote.ui.fragments.firestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.ui.fragments.settings.SettingsViewModel

class FireStoreViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FireStoreViewModel::class.java)) {
            return FireStoreViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}