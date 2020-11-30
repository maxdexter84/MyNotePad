package com.maxdexter.mynote.repository

import android.net.Uri
import com.maxdexter.mynote.model.Note

import com.maxdexter.mynote.data.provider.FireStoreProvider
import com.maxdexter.mynote.data.provider.RemoteDataProvider
import java.io.File


object Repository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()
    fun synchronization() = remoteProvider.subscribeToAllNotes()
    fun saveNoteInFireStore(note: Note) = remoteProvider.saveNote(note)
    fun getNoteByIdFromFireStore(uuid: String) = remoteProvider.getNoteById(uuid)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun loadToFireStore(allNotes: List<Note>) {
        allNotes.forEach { note -> saveNoteInFireStore(note) }
    }



}