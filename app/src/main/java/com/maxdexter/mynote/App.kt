package com.maxdexter.mynote

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maxdexter.mynote.database.AppDatabase

class App : Application(){

    companion object{
        lateinit var database: AppDatabase
        var instance: App? = null
//        fun applicationContext(): Context? {
//            return instance?.applicationContext
//
//        }
    }
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE note ADD COLUMN photoFilename TEXT NOT NULL DEFAULT''")
//            }
//        }
//                .addMigrations(MIGRATION_1_2)
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .build()

    }
//    fun getDatabase(): AppDatabase {
//        return database ?: error("no database")
//    }




}