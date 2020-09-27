package com.maxdexter.mynote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxdexter.mynote.extensions.currentDate
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Note(@PrimaryKey(autoGenerate = true)
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
                var date: String = Date().currentDate()) {

    val photoFilename: String
        get() = "IMG $uuid .jpg"

}