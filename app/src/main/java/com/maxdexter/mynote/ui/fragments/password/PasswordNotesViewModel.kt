package com.maxdexter.mynote.ui.fragments.password

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.ui.fragments.simple.SIMPLE_NOTE
const val PASSWORD_NOTE = 2
class PasswordNotesViewModel(private val context: Context) : ViewModel() {

    private val _passwordNote = MutableLiveData<List<Note>>()
    val passwordNote: LiveData<List<Note>>
        get() = _passwordNote

    init {

        _passwordNote.value = getSimpleNote()

    }

    private fun getSimpleNote(): List<Note> {
        return NotePad.get(context).notes.filter { it.typeNote == PASSWORD_NOTE }
    }
}