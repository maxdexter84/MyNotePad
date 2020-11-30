package com.maxdexter.mynote.data

import androidx.lifecycle.LiveData
import com.maxdexter.mynote.App
import com.maxdexter.mynote.database.AppDatabase
import com.maxdexter.mynote.model.Note

class NoteRepository private constructor() {

    val database:AppDatabase = App.database
    val notes
        get() = database.mNoteDao().getAll()

    fun getNote(id: String): LiveData<Note> {
        return database.mNoteDao().getById(id)
    }

   suspend fun addNote(n: Note) {
        database.mNoteDao().insert(n)
    }

   suspend fun updateNote(n: Note) {
        database.mNoteDao().update(n)

    }

   suspend fun deleteNote(n: Note) {
        database.mNoteDao().delete(n)
    }


    companion object {
        private var notePad: NoteRepository? = null
        @JvmStatic
        fun get(): NoteRepository? { //Если экземпляр уже существует то просто возвращает его, если не существует то вызвывается конструктор для его создания
            if (notePad == null) {
                notePad = NoteRepository()
            }
            return notePad
        }
    }

}