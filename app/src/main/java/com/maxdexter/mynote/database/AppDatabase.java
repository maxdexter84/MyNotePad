package com.maxdexter.mynote.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.maxdexter.mynote.data.Note;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao mNoteDao();
}
