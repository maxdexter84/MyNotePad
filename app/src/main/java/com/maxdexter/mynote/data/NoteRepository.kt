package com.maxdexter.mynote.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.maxdexter.mynote.App
import com.maxdexter.mynote.database.AppDatabase
import com.maxdexter.mynote.model.Note

class NoteRepository private constructor(context: Context) {

    val database:AppDatabase = AppDatabase.invoke(context)
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
        fun get(context: Context): NoteRepository? { //Если экземпляр уже существует то просто возвращает его, если не существует то вызвывается конструктор для его создания
            if (notePad == null) {
                notePad = NoteRepository(context)
            }
            return notePad
        }
    }

}