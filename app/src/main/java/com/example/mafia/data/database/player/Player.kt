package com.example.mafia.data.database.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "game_id")
    val gameId: Int,
    @ColumnInfo(name = "number")
    val number: Int,
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @ColumnInfo(name = "role")
    val role: String,
    @ColumnInfo(name = "points")
    val points: Int,
    @ColumnInfo(name = "status")
    val isDead: String
)
