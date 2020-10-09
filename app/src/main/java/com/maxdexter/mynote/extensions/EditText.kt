package com.maxdexter.mynote.extensions

import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.EditText
import com.maxdexter.mynote.model.Note

fun EditText.setTitle (note: Note) {
    setText(note.title)
}

fun EditText.setDescription(note: Note) {
    setText(note.description)
}

fun EditText.setTextSize(size:Int) {
    when(size) {
        0 -> this.textSize = 16F
        1 -> this.textSize = 18F
        2 -> this.textSize = 20F
    }
}

fun EditText.setImage(uuid: String,uri: Uri) {
    val imageSpan = ImageSpan(context,uri)
    val spannableStringBuilder = SpannableStringBuilder()
    spannableStringBuilder.append(this.text)
    val selStart = this.selectionStart
    spannableStringBuilder.replace(this.selectionStart, this.selectionEnd, uuid)
    spannableStringBuilder.setSpan(imageSpan,selStart, selStart + uuid.length,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = spannableStringBuilder
}