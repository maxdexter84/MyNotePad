package com.maxdexter.mynote.extensions

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.maxdexter.mynote.R
import com.maxdexter.mynote.utils.DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun Date.currentDate(): String{
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(Date())
}

