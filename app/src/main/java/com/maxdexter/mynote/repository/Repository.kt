package com.maxdexter.mynote.repository

import androidx.lifecycle.LiveData
import com.maxdexter.mynote.data.Note
import com.maxdexter.mynote.data.provider.FireStoreProvider
import com.maxdexter.mynote.data.provider.RemoteDataProvider
import com.maxdexter.mynote.model.NoteResult

object Repository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

}