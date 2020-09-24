package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note


class NoteListFragmentViewModel(private val typeNote: Int, private val context: Context): ViewModel() {

    private val _allNoteList = MutableLiveData<List<Note>>()
    val allNoteList: LiveData<List<Note>>
        get() = _allNoteList

    init {

        _allNoteList.value = getSimpleNote()

    }

    private fun getSimpleNote(): List<Note> {
        return NotePad.get(context).notes.filter { it.typeNote == typeNote }
    }


}