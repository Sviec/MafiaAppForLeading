package com.example.mafia.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    fun getAll(): Flow<List<Game>>
    @Insert
    fun insert(game: Game)
    @Delete
    fun delete(game: Game)
}