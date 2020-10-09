package com.maxdexter.mynote

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.maxdexter.mynote.database.AppDatabase

class App : Application(){

    companion object{
        lateinit var database: AppDatabase
        var instance: App? = null
        fun applicationContext(): Context? {
            return instance?.applicationContext

        }
    }
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .build()

    }
    fun getDatabase(): AppDatabase {
        return database ?: error("no database")
    }




}