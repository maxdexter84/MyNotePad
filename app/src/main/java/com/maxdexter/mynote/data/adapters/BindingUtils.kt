package com.maxdexter.mynote.data.adapters


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.maxdexter.mynote.model.Note

@BindingAdapter("setTitle")
fun TextView.setTitle (note: Note) {
    this.text = note.title
}
@BindingAdapter("setDate")
fun TextView.setDate(note: Note) {
    this.text = note.date
}


