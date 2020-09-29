package com.maxdexter.mynote.utils

import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

enum class DetailEvent() {
    PHOTO, GALLERY, SHARE, DELETE, SAVE,ZOOM_IMAGE
}

enum class NoteListEvent() {
    CHANGE_DISPLAY
}