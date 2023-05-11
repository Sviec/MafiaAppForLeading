package com.example.mafia

import android.app.Application
import androidx.room.Room
import com.example.mafia.data.database.AppDatabase

class App : Application() {
    lateinit var db: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "db"
            )
            .build()
    }

}