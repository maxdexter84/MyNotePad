package com.maxdexter.mynote.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.maxdexter.mynote.database.AppDatabase
import com.maxdexter.mynote.model.Note
import java.io.File

class NotePad private constructor(val context: Context) {

    val database: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database").fallbackToDestructiveMigration()
            .build()
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

    fun getPhotoFile(note: Note): File {
        val filesDir = context.filesDir
        return File(filesDir, note.photoFilename)
    }

    companion object {
        private var notePad: NotePad? = null
        @JvmStatic
        operator fun get(context: Context): NotePad? { //Если экземпляр уже существует то просто возвращает его, если не существует то вызвывается конструктор для его создания
            if (notePad == null) {
                notePad = NotePad(context)
            }
            return notePad
        }
    }

}