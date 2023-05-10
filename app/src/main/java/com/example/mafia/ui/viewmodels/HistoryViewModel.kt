package com.example.mafia.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafia.data.database.game.Game
import com.example.mafia.data.database.game.GameDao
import com.example.mafia.data.database.player.Player
import com.example.mafia.data.database.player.PlayerDao
import com.example.mafia.entity.PlayerInfo
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime

class HistoryViewModel(
    private val gameDao: GameDao,
    private val playerDao: PlayerDao
    ): ViewModel() {

    val allGames: StateFlow<List<Game>> = this.gameDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun getPlayers(gameId: Int) : Flow<List<Player>> {
        return playerDao.getAll(gameId)
    }

    fun addGame(playersInfo: List<PlayerInfo>) {
        gameDao.insert(
            Game(
                id = 0,
                date = LocalDate.now().toString(),
                time = LocalTime.now().toString(),
                playersCount = playersInfo.size
            )
        )
        playersInfo.forEach {
            playerDao.insert(
                Player(
                    id = 0,
                    gameId = 0,
                    nickname = it.nickname,
                    number = it.number,
                    role = it.role!!,
                    points = it.points,
                    isDead = if (it.isDead) "Мертв" else "Жив"
                )
            )
        }
    }

    fun deleteGame(game: Game) {
        gameDao.delete(game)
//        for (i in 0 until game.playersCount)
//            playerDao.delete()
    }
}