package com.maxdexter.mynote.ui.fragments.simple

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note
const val SIMPLE_NOTE = 0
class SimpleNoteViewModel(private val context: Context) : ViewModel() {

    private val _simpleNote = MutableLiveData<List<Note>>()
        val allNoteList: LiveData<List<Note>>
            get() = _simpleNote

    init {

        _simpleNote.value = getSimpleNote()

    }

    private fun getSimpleNote(): List<Note> {
       return NotePad.get(context).notes.filter { it.typeNote == SIMPLE_NOTE }
    }

}