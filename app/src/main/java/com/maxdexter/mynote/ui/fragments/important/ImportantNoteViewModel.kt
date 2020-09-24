package com.maxdexter.mynote.ui.fragments.important

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.ui.fragments.password.PASSWORD_NOTE

const val IMPORTANT_NOTE = 1
class ImportantNoteViewModel(private val context: Context, val notePad: NotePad = NotePad.get(context)) : ViewModel() {


    private val _importantNote = MutableLiveData<List<Note>>()
    val importantNote: LiveData<List<Note>>
        get() = _importantNote

    init {

        _importantNote.value = getSimpleNote()

    }

    private fun getSimpleNote(): List<Note> {
        return NotePad.get(context).notes.filter { it.typeNote == IMPORTANT_NOTE }
    }



}