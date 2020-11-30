package com.maxdexter.mynote.extensions

import android.widget.RadioGroup
import com.maxdexter.mynote.model.Note
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.settings_fragment.view.*

fun RadioGroup.selectItem(note: Note){
    when(note.typeNote) {
        0 -> this.simple_radio_btn.isChecked = true
        1 -> this.important_radio_btn.isChecked = true
        2 -> this.password_radio_btn.isChecked = true
    }
}

fun RadioGroup.checkedRB(position: Int){
    when(position) {
        0 -> this.rb_small_text.isChecked = true
        1 -> this.rb_medium_text.isChecked = true
        2 -> this.rb_large_text.isChecked = true
    }
}