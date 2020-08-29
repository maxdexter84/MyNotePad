package com.maxdexter.mynote.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.maxdexter.mynote.database.AppDatabase;

import java.io.File;
import java.util.List;

public class NotePad {
    MutableLiveData<Integer> mLiveData;

    public MutableLiveData<Integer> getLiveData() {
        return mLiveData;
    }

    public void setLiveData(int i) {
        mLiveData.setValue(i);
    }

    private static NotePad sNotePad;
    Context mContext;
    private AppDatabase database;
    private List<Note>mNotes;
    public List<Note> getNotes() {
        mNotes = database.mNoteDao().getAll();
        return mNotes;
    }
    public static NotePad get(Context context){ //Если экземпляр уже существует то просто возвращает его, если не существует то вызвывается конструктор для его создания
        if(sNotePad == null){
            sNotePad = new NotePad(context);
        }
        return sNotePad;
    }
    private NotePad(Context context){
        mContext = context;
        mLiveData = new MutableLiveData<>();
        database = Room.databaseBuilder(context, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

    }

    public Note getNote(String id){
        return database.mNoteDao().getById(id);
    }
    public void addNote(Note n){
        database.mNoteDao().insert(n);
        getNotes();
    }
    public void updateNote(Note n){
        database.mNoteDao().update(n);
        getNotes();
    }
    public void deleteNote(Note n){
        database.mNoteDao().delete(n);
        getNotes();
    }
    public AppDatabase getDatabase() {
        return database;
    }

    public File getPhotoFile(Note note){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir,note.getPhotoFilename());

    }


}
