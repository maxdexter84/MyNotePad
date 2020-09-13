package com.maxdexter.mynote.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.currentDate(): String{
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(Date())
}