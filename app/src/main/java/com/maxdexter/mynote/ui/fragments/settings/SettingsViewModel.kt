package com.maxdexter.mynote.ui.fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private var _toFireStore = MutableLiveData<Boolean>()
            val toFireStore: LiveData<Boolean>
                get() = _toFireStore
    init {
        _toFireStore.value = false
    }

    fun navigateToFireStore() {
        _toFireStore.value = true
    }
}