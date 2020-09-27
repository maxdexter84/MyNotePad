package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NotePad
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

        _allNoteList.value = getNoteList()

    }

    private fun getNoteList(): List<Note> =
             when(typeNote){
                -1 ->  NotePad.get(context).notes
                else -> NotePad.get(context).notes.filter { it.typeNote == typeNote }
            }

}