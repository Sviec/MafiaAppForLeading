package com.example.mafia.data.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mafia.data.database.game.Game
import com.example.mafia.data.database.player.Player

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
