package com.maxdexter.mynote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoteListActivityViewModel: ViewModel() {

    private val _changeBehavior = MutableLiveData<Boolean>()
            val changeBehavior: LiveData<Boolean>
            get() = _changeBehavior
    private var stateAppBar = true

    init {
//        _changeBehavior.value = stateAppBar
    }

     fun clickFAB(){
        if (stateAppBar) {
            stateAppBar = false
            _changeBehavior.value = stateAppBar
        }  else {
            stateAppBar = true
            _changeBehavior.value = stateAppBar
        }
    }
}