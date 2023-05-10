package com.example.mafia

import android.app.Application
import androidx.room.Room
import com.example.mafia.data.database.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        db = Room
            .inMemoryDatabaseBuilder(
                applicationContext,
                AppDatabase::class.java
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        lateinit var INSTANCE: App
            private set
    }
}