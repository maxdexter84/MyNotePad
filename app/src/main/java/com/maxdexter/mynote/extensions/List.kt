package com.maxdexter.mynote.extensions

import android.provider.ContactsContract
import com.maxdexter.mynote.model.Note

fun List<String>.getImageList(photoFilename: String): MutableList<String> {
    var list = mutableListOf<String>()
    if (photoFilename != ""){
        list = (photoFilename.split(",") ?: mutableListOf<String>()) as MutableList<String>
    }
    return list
}

fun List<String>.deleteImageFromList(photoUriList: MutableList<String>, element:String): String {
    var list = mutableListOf<String>()
    list.addAll(photoUriList)
    list.remove(element)
    var str = ""
    list.forEach {
        str += "$it,"
    }
    return str.trim(',')
}
