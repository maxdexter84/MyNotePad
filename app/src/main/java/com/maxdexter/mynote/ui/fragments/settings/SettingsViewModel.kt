package com.maxdexter.mynote.ui.fragments.settings

import android.content.Context
import androidx.lifecycle.*
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SettingsViewModel(private val repository: Repository?, private val owner: LifecycleOwner, private val context: Context?) : ViewModel() {
    private var viewModelJob = Job() //когда viewModel будет уничтожена то в переопределенном методе onCleared() будут так же завершены все задания
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var notes = NotePad.get(context)?.notes

    private var _isAuth = MutableLiveData<Boolean>()
    val isAuth: LiveData<Boolean>
        get() = _isAuth

    private var _logOut = MutableLiveData<Boolean>()
    val logOut: LiveData<Boolean>
        get() = _logOut


    init {
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
        var list = notes?.toList()
        if (list != null) {
            repository?.loadToFireStore(list)
        }
    }

    fun downloadFromFireStore(){
        var listOfNote = mutableListOf<Note>()
        repository?.synchronization()?.observe(owner, Observer{it-> listOfNote = it})
        uiScope.launch {listOfNote.forEach{note ->  NotePad.get(context).addNote(note) }  }


    }

    fun logoutDialog(){

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}