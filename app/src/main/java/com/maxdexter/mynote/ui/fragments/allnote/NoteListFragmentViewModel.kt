package com.maxdexter.mynote.ui.fragments.allnote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note

class NoteListFragmentViewModel(private val context: Context): ViewModel() {

    private val _allNoteList: MutableLiveData<MutableList<Note>> = NotePad.get(context).liveNotes
    val allNoteList: LiveData<MutableList<Note>>
            get() = _allNoteList

}