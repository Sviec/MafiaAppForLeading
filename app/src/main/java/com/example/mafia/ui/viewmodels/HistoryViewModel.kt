package com.example.mafia.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mafia.data.database.game.Game
import com.example.mafia.data.database.game.GameDao
import com.example.mafia.data.database.player.Player
import com.example.mafia.data.database.player.PlayerDao
import com.example.mafia.entity.PlayerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryViewModel(
    private val gameDao: GameDao,
    private val playerDao: PlayerDao
) : ViewModel() {
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    val allGames: StateFlow<List<Game>> = this.gameDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun getPlayers(gameDate: String) : StateFlow<List<Player>> {
        return playerDao.getAll(gameDate).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }

    fun addGame(playersInfo: List<PlayerInfo>) {
        val date = LocalDateTime.now().format(formatter)
        viewModelScope.launch(Dispatchers.IO) {
            gameDao.insert(
                Game(
                    date = date,
                    playersCount = playersInfo.size
                )
            )
            playersInfo.forEach {
                playerDao.insert(
                    Player(
                        gameDate = date,
                        nickname = it.nickname,
                        number = it.number,
                        role = it.role!!,
                        points = it.points,
                        isDead = if (it.isDead) "Мертв" else "Жив"
                    )
                )
            }
        }

    }

    fun deleteGame(gameDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gameDao.delete(gameDate)
            val players = getPlayers(gameDate)
            for (player in players.value) {
                playerDao.delete(player)
            }
        }
    }
}