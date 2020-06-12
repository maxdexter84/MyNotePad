package com.maxdexter.mynote;

import android.app.Activity;
import android.content.SharedPreferences;


public class SharedPref {

    //Имя параметра в настройках
    private  static final String TEXT_SIZE = "text_size";
    private static final String IsDarkThem = "IS_DARK_THEM";
    SharedPreferences sharedPref;
    public SharedPref(Activity activity) {
        sharedPref = activity.getPreferences(Activity.MODE_PRIVATE);
    }


    //Чтенеие настроек
    public boolean isDarkTheme(){
        // Если настройка не найдена , то берется параметр по умолчанию
        return sharedPref.getBoolean(IsDarkThem,true);
    }
    public float getTextSize(){
        return sharedPref.getFloat(TEXT_SIZE,18f);
    }

    //Сохранение настроек
    public void setDarkTheme(boolean isDarkTheme){
        sharedPref.edit().putBoolean(IsDarkThem,isDarkTheme).apply();
        //Параметры сохраняются посредствам специального класса editor
    }

    public void setTextSize(float textSize){
        sharedPref.edit().putFloat(TEXT_SIZE,textSize).apply();
    }
}
