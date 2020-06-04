package com.maxdexter.mynote.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
@Entity
public class Note {


    public int getId() {
        return id;
    }



    public void setDate(String date) {
        mDate = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "mUUID")
    private String mUUID;
    @ColumnInfo(name = "type_note")
    private int mTypeNote;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "date")
    private String mDate;

    public int getTypeNote() {
        return mTypeNote;
    }

    public void setTypeNote(int typeNote) {
        mTypeNote = typeNote;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = dateFormat(date);
    }


    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String uuid) {
        mUUID = uuid;
    }

    public Note(){
        mUUID = UUID.randomUUID().toString();
        Date date = new Date();
        mDate = dateFormat(date);
    }
    private String dateFormat(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("d MMM yy HH:mm", Locale.getDefault());
       String time = sd.format(date);
       return time;
    }


}
