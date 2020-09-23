package com.maxdexter.mynote.ui.fragments.important

import android.content.Context
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.repository.Repository
const val IMPORTANT_NOTE = 1
class ImportantNoteViewModel(private val typeNote: Int, context: Context, val notePad: NotePad = NotePad.get(context)) : ViewModel() {



}