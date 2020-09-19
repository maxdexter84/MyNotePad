package com.maxdexter.mynote

import android.app.Application
import androidx.room.Room
import com.maxdexter.mynote.database.AppDatabase

class App : Application(){
    var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
       database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .allowMainThreadQueries()
                .build()
    }

    @JvmName("getDatabase1")
    fun getDatabase(): AppDatabase?{
        if (database == null) {
            database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                    .allowMainThreadQueries()
                    .build()
        }
        return database
    }


}