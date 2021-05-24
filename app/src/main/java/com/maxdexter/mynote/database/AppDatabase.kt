package com.maxdexter.mynote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maxdexter.mynote.model.Note

@Database(entities = [Note::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mNoteDao(): NoteDao


    companion object{
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context,
                AppDatabase::class.java,
                "article_db.db").build()
    }
}