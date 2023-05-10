package com.example.mafia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mafia.data.database.game.Game
import com.example.mafia.data.database.game.GameDao
import com.example.mafia.data.database.player.PLayerDao
import com.example.mafia.data.database.player.Player

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
    abstract fun playerDao(): PLayerDao
}