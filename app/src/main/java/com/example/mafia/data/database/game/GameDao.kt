package com.example.mafia.data.database.game

import androidx.room.*
import com.example.mafia.data.database.GameWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Transaction
    @Query("SELECT * FROM games")
    fun getAll(): Flow<List<Game>>
    @Insert
    fun insert(game: Game)
    @Query("DELETE FROM games WHERE :gameDate == date")
    fun delete(gameDate: String)
}