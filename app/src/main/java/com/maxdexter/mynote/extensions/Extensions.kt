package com.maxdexter.mynote.extensions

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.maxdexter.mynote.R
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.DATE_TIME_FORMAT
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.text.SimpleDateFormat
import java.util.*

fun Date.currentDate(): String{
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(Date())
}

fun RadioGroup.selectItem(note: Note){
    when(note.typeNote) {
        0 -> this.simple_radio_btn.isChecked = true
        1 -> this.important_radio_btn.isChecked = true
        2 -> this.password_radio_btn.isChecked = true
    }
}

fun EditText.setTitle (note: Note) {
    text.append(note.title)
}

fun EditText.setDescription(note: Note) {
    text.append(note.description)
}

fun <T> ImageView.setImage(context: Context, uri: T){
    Glide.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
}




