package com.example.mafia.data.database.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE :gameId == game_id")
    fun getAll(gameId: Int): Flow<List<Player>>
    @Insert
    fun insert(player: Player)
    @Delete
    fun delete(player: Player)
}