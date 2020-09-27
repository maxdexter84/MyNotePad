package com.maxdexter.mynote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
class Note( @PrimaryKey(autoGenerate = true)
            var id: Int = 0,
            @ColumnInfo(name = "uuid")
            var uuid: String = UUID.randomUUID().toString(),
            @ColumnInfo(name = "type_note")
            var typeNote: Int = 0,
            @ColumnInfo(name = "title")
            var title: String = "",
            @ColumnInfo(name = "description")
            var description: String = "",
            @ColumnInfo(name = "date")
            var date: String ) {

    private fun dateFormat(date: Date): String {
        val sd = SimpleDateFormat("d MMM yy HH:mm", Locale.getDefault())
        return sd.format(date)
    }

    val photoFilename: String
        get() = "IMG $uuid .jpg"

    init {
        val date = Date()
        this.date = dateFormat(date)
    }
}