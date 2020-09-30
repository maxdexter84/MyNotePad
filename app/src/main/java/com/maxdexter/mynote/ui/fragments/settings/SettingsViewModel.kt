package com.maxdexter.mynote.ui.fragments.settings

import android.content.Context
import androidx.lifecycle.*
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.utils.SettingsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SettingsViewModel(private val repository: Repository?, private val owner: LifecycleOwner, private val context: Context?) : ViewModel() {
    private var viewModelJob = Job() //когда viewModel будет уничтожена то в переопределенном методе onCleared() будут так же завершены все задания
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var notes = context?.let { NotePad.get(it)?.notes }

    private var _isAuth = MutableLiveData<Boolean>()
    val isAuth: LiveData<Boolean>
        get() = _isAuth

    private var _settingsEvent = MutableLiveData<SettingsEvent>()
    val settingsEvent: LiveData<SettingsEvent>
        get() = _settingsEvent

    private var _logOut = MutableLiveData<Boolean>()
    val logOut: LiveData<Boolean>
        get() = _logOut


    init {
        _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        _logOut.value = false
        isAuthFunc()
    }

    private fun isAuthFunc() {
        repository?.getCurrentUser()?.observe(owner, Observer { _isAuth.value = it != null })
    }

    fun logOut(){
        _logOut.value = true
    }

    fun onLoadToFireStore() {
        _settingsEvent.value = SettingsEvent.LOAD_TO_FIRE_STORE
        notes?.observeForever {
            repository?.loadToFireStore(it)
        }
        _settingsEvent.value = SettingsEvent.CANCEL_EVENT
    }

    fun downloadFromFireStore(){
        _settingsEvent.value = SettingsEvent.LOAD_FROM_FIRE_STORE
        var listOfNote = mutableListOf<Note>()
        repository?.synchronization()?.observe(owner, Observer{ listOfNote = it
            _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        })
        uiScope.launch {listOfNote.forEach{note ->
            if (context != null) {
                NotePad.get(context)?.addNote(note)
            }
        }  }
    }

    override fun onCleared() {
        super.onCleared()
        _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        viewModelJob.cancel()
    }
}