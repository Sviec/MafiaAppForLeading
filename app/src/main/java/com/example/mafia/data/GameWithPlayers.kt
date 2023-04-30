package com.example.mafia.data

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithPlayers(
    @Embedded
    val game: Game,
    @Relation(
        entity = Player::class,
        parentColumn = "id",
        entityColumn = "game_id"
    )
    val players: List<Player>

)
