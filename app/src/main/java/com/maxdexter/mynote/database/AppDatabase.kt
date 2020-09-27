package com.maxdexter.mynote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxdexter.mynote.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mNoteDao(): NoteDao?
}