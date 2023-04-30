package com.example.mafia.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Game::class,
        Player::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}