package com.maxdexter.mynote.extensions

import android.widget.EditText
import com.maxdexter.mynote.model.Note

fun EditText.setTitle (note: Note) {
    text.append(note.title)
}

fun EditText.setDescription(note: Note) {
    text.append(note.description)
}

fun EditText.setTextSize(size:Int) {
    when(size) {
        0 -> this.textSize = 16F
        1 -> this.textSize = 18F
        2 -> this.textSize = 20F
    }
}
