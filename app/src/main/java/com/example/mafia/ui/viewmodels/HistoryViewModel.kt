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

class HistoryViewModel(
    private val gameDao: GameDao,
    private val playerDao: PlayerDao
) : ViewModel() {

    val allGames: StateFlow<List<Game>> = this.gameDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players = _players.asStateFlow()

    fun getPlayers(gameDate: String) : StateFlow<List<Player>> {
        return playerDao.getAll(gameDate).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }

    fun addGame(playersInfo: List<PlayerInfo>) {
        val date = LocalDateTime.now().toString()
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

    fun deleteGame(game: Game) {
        gameDao.delete(game)
//        for (i in 0 until game.playersCount)
//            playerDao.delete()
    }
}