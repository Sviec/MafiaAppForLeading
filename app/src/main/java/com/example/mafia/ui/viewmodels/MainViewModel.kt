package com.example.mafia.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mafia.entity.Player
import com.example.mafia.entity.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val currentPlayersList = mutableListOf<Player>()
    private val roles = mutableSetOf<Role>()

    private val _currentPlayersFlow = MutableStateFlow<List<Player>>(emptyList())
    val currentPlayersFlow = _currentPlayersFlow.asStateFlow()

    fun addPlayer(player: Player) {
        currentPlayersList.add(player)
        _currentPlayersFlow.value = currentPlayersList.toList()
    }

    fun deletePlayer(player: Player) {
        currentPlayersList.remove(player)
        _currentPlayersFlow.value = currentPlayersList.toList()
    }

}