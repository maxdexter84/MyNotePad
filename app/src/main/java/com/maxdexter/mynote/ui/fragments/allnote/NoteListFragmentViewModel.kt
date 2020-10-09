package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NoteRepository
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.NoteListEvent


class NoteListFragmentViewModel(private val typeNote: Int, private val context: Context): ViewModel() {

    private val _allNoteList = MutableLiveData<List<Note>>()
    val allNoteList: LiveData<List<Note>>
        get() = _allNoteList

    private val _eventNoteList = MutableLiveData<NoteListEvent>()
            val eventNoteList: LiveData<NoteListEvent>
                get() = _eventNoteList

    init {
        getNoteList()
    }

    private fun getNoteList(){
        var list: List<Note>
        NoteRepository.get()?.notes?.observeForever { list = it
            _allNoteList.value =  when(typeNote){
                -1 ->  list
                else -> list.filter { it.typeNote == typeNote }
            }
        }
    }


}