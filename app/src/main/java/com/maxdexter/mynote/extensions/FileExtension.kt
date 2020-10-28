package com.maxdexter.mynote.extensions

import android.content.Context
import java.io.File
fun File.getFile(uri:String, context:Context): File {
    val storageDir: File? = context.filesDir
    return File(storageDir, uri)
}