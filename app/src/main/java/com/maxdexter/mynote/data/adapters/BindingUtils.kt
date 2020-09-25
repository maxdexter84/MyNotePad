package com.maxdexter.mynote.data.adapters


import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.maxdexter.mynote.R
import com.maxdexter.mynote.model.Note

@BindingAdapter("setTitle")
fun TextView.setTitle (note: Note) {
    this.text = note.title
}
@BindingAdapter("setDate")
fun TextView.setDate(note: Note) {
    this.text = note.date
}

@BindingAdapter("cardViewColor")
fun CardView.setBackgroundColor (note: Note) {
    val color = when(note.typeNote) {
        0 -> R.color.color_green
        1 -> R.color.color_red
        2 -> R.color.color_yello
        else -> R.color.color_blue
    }
    setCardBackgroundColor(resources.getColor(color))
    //setBackgroundColor(resources.getColor(color))
}
