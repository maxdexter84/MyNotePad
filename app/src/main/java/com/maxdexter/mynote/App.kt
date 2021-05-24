package com.maxdexter.mynote

import android.app.Application
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class App : Application(){


    override fun onCreate() {
        super.onCreate()
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN noteLabel TEXT NOT NULL DEFAULT''")
            }
        }

//        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
//                .addMigrations(MIGRATION_1_2)
//                .build()

    }
//    fun getDatabase(): AppDatabase {
//        return database ?: error("no database")
//    }


//    companion object{
//        lateinit var database: AppDatabase
//        var instance: App? = null
////        fun applicationContext(): Context? {
////            return instance?.applicationContext
////
////        }
//    }
//    init {
//        instance = this
//    }




}