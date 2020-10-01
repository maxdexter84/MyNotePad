package com.maxdexter.mynote.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
fun Activity.hideKeyboard() {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}