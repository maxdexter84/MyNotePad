package com.maxdexter.mynote.extensions

import android.provider.ContactsContract
import com.maxdexter.mynote.model.Note

fun List<String>.getImageList(photoFilename: String): List<String> {
    var list = listOf<String>()
    if (photoFilename != ""){
        list = photoFilename.split(",") ?: listOf<String>()
    }
    return list
}