package com.maxdexter.mynote.ui.fragments.settings

import android.app.Activity
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.maxdexter.mynote.SharedPref
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.utils.SettingsEvent
import kotlinx.coroutines.*


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

    private var _textSize = MutableLiveData<Int>()
    val textSize: LiveData<Int>
        get() = _textSize


    init {
        _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        _logOut.value = false
        _textSize.value = -1
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
            _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        }

    }

    fun downloadFromFireStore(){
        _settingsEvent.value = SettingsEvent.LOAD_FROM_FIRE_STORE
        repository?.synchronization()?.observe(owner, Observer{
            if (it != null){
                it.forEach {note -> uiScope.launch {
                    if (context != null) {
                        NotePad.get(context)?.addNote(note)
                    }
                } }
                _settingsEvent.value = SettingsEvent.CANCEL_EVENT
            }
        })
    }

    fun changeTextSize(position: Int, activity: Activity) {
        when (position) {
            0 -> SharedPref(activity).textSize = 0
            1 -> SharedPref(activity).textSize = 1
            2 -> SharedPref(activity).textSize = 2
        }
    }


    override fun onCleared() {
        super.onCleared()
        _settingsEvent.value = SettingsEvent.CANCEL_EVENT
        viewModelJob.cancel()
    }

}