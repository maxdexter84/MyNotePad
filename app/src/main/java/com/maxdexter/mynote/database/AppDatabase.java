package com.maxdexter.mynote.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.maxdexter.mynote.data.Note;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao mNoteDao();

}
