package com.maxdexter.mynote.extensions

import com.maxdexter.mynote.utils.DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

fun Date.currentDate(): String{
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(Date())
}