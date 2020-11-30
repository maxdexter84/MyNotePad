package com.maxdexter.mynote.data.provider

import android.net.Uri
import androidx.lifecycle.LiveData
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.model.User


interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<MutableList<Note>>
    fun getNoteById(uuid: String): LiveData<Note>
    fun saveNote(note: Note) : LiveData<Note>
    fun getCurrentUser(): LiveData<User?>
    suspend fun deleteNote(note: Note): Boolean
}
