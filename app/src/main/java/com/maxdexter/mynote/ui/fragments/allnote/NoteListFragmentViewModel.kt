package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.data.NoteRepository
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.NoteListEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class NoteListFragmentViewModel(private val typeNote: Int, private val owner: LifecycleOwner): ViewModel() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    var deleteNote: Boolean = false


    private val _allNoteList = MutableLiveData<List<Note>>()
    val allNoteList: LiveData<List<Note>>
        get() = _allNoteList

    


    init {
        getNoteList()

    }

    private fun getNoteList(){
        var list: List<Note>
        NoteRepository.get()?.notes?.observe (owner){ observeList ->
            list = observeList
            _allNoteList.value =  when(typeNote){
                -1 ->  list
                else -> list.filter { it.typeNote == typeNote }
            }
        }
    }

    fun deleteNote(note: Note, view: View) {
        Snackbar.make(view, "Точно удалить?", Snackbar.LENGTH_LONG).setAction("Да") {
            scope.launch {
                NoteRepository.get()?.deleteNote(note)
            }
        }.show()

    }

    override fun onCleared() {
        super.onCleared()
       job.cancel()
    }


}