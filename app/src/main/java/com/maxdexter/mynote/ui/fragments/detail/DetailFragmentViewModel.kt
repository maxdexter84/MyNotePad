package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.extensions.currentDate
import com.maxdexter.mynote.utils.DetailEvent
import java.util.*


class DetailFragmentViewModel(uuid: String, private val context: Context) : ViewModel() {
    lateinit var note: Note
    private val _newNote = MutableLiveData<Note>()
            val newNote: LiveData<Note>
            get() = _newNote

    private val _eventType = MutableLiveData<DetailEvent>()
            val eventType: LiveData<DetailEvent>
            get() = _eventType

    init {
        selectNote(uuid)
    }

    private fun selectNote(uuid: String) {
        if (uuid == com.maxdexter.mynote.utils.NEW_NOTE) {
            note = Note()
            _newNote.value = note
        } else {
            note = NotePad.get(context).getNote(uuid)
            _newNote.value = note
        }
    }


    fun deleteNote() {
       NotePad.get(context).deleteNote(note)
    }

    fun changeTitle(title: String) {
        note.title = title
    }

    fun changeDescription(text: String) {
        note.description = text
    }

    fun changeType(type: Int) {
        note.typeNote = type
    }

    private fun changeDate() {
        note.date = Date().currentDate()
    }

    fun saveNote() {
        changeDate()
        if (note.title != "" || note.description != ""){
            NotePad.get(context).addNote(note)
        }

    }

    override fun onCleared() {
        super.onCleared()

        saveNote()
    }
}