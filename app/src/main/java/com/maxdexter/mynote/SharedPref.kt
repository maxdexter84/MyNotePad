package com.maxdexter.mynote

import android.app.Activity
import android.content.SharedPreferences

class SharedPref(activity: Activity) {
    var sharedPref: SharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE)// Если настройка не найдена , то берется параметр по умолчанию//Параметры сохраняются посредствам специального класса editor

    //Сохранение настроек
    //Чтенеие настроек
    var isDarkTheme: Boolean
        get() =// Если настройка не найдена , то берется параметр по умолчанию
            sharedPref.getBoolean(IsDarkThem, false)
        set(isDarkTheme) {
            sharedPref.edit().putBoolean(IsDarkThem, isDarkTheme).apply()
            //Параметры сохраняются посредствам специального класса editor
        }
    var textSize: Int
        get() = sharedPref.getInt(TEXT_SIZE, 0)
        set(textSize) {
            sharedPref.edit().putInt(TEXT_SIZE, textSize).apply()
        }

    companion object {
        //Имя параметра в настройках
        private const val TEXT_SIZE = "text_size"
        private const val IsDarkThem = "IS_DARK_THEM"
    }

}