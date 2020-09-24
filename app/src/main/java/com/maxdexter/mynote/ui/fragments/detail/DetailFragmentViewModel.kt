package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.extensions.currentDate
import java.util.*


class DetailFragmentViewModel(private val uuid: String, private val context: Context) : ViewModel() {
    lateinit var note: Note
    private val _newNote = MutableLiveData<Note>()
            val newNote: LiveData<Note>
            get() = _newNote



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

    fun changeTitle(title: String) {
        note.title = title
    }

    fun changeDescription(text: String) {
        note.description = text
    }

    fun changeType(type: Int) {
        note.typeNote = type
    }

    fun changeDate() {
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