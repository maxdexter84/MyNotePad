package com.maxdexter.mynote.model;




import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.maxdexter.mynote.extensions.ExtensionsKt;


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

//    public void setDate(Date date) {
//        mDate = dateFormat(date);
//    }


    public String getUUID() {
        return mUUID;
    }

    public void setUUID(String uuid) {
        mUUID = uuid;
    }

    public Note(){
        this.mUUID = UUID.randomUUID().toString();
        this.mTypeNote = 0;
        this.mTitle = "";
        this.id = 0;
        this.mDescription = "";
        Date date = new Date();
        //setDate(date);
        setDate(dateFormat(date));
    }


    private String dateFormat(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("d MMM yy HH:mm", Locale.getDefault());
        return sd.format(date);
    }

    public String getPhotoFilename(){
        return "IMG " + getUUID() + " .jpg";
    }


}
