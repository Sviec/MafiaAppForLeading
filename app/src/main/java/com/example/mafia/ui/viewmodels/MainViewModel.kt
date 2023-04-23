package com.example.mafia.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mafia.entity.Player
import com.example.mafia.entity.Roles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val currentPlayersList = mutableListOf<Player>()

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

    fun setRoles() {
        Roles.values().forEach {
            it.players = currentPlayersFlow.value.filter { player ->
                player.role == it.name_ru
            }
        }
    }

    fun nightResults() {
        Roles.values().filter { it.purpose != null }.forEach {
            when (it) {
                Roles.Mafia -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        it.purpose!!.isDead = true
                        Roles.Whore.purpose!!.isDead = true
                    } else if (it.purpose != Roles.Whore.purpose) {
                        it.purpose!!.isDead = true
                    }
                }
                Roles.Maniac -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        it.purpose!!.isDead = true
                        Roles.Whore.purpose!!.isDead = true
                    } else if (it.purpose != Roles.Whore.purpose) {
                        it.purpose!!.isDead = true
                    }
                }
                Roles.Doctor -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        it.purpose!!.isDead = false
                        Roles.Whore.purpose!!.isDead = false
                    } else {
                        it.purpose!!.isDead = false
                    }
                    if ((it.purpose == Roles.Mafia.purpose || it.purpose == Roles.Maniac.purpose) &&
                        it.purpose!!.role != Roles.Mafia.name_ru ||
                        it.purpose!!.role != Roles.Maniac.name_ru
                    ) {
                        it.players.forEach { player ->
                            player.points++
                        }
                    }
                }
                Roles.Whore -> {
                    if (it.purpose == Roles.Mafia.purpose || it.purpose == Roles.Maniac.purpose &&
                        it.players.count { player -> !player.isDead } > 0
                    ) {
                        it.players.forEach { player ->
                            player.points++
                        }
                    } else if (it.players.count { player -> player.isDead } > 0 &&
                        (it.purpose!!.role == Roles.Mafia.name_ru ||
                                it.purpose!!.role == Roles.Maniac.name_ru)
                    ) {
                        it.players.forEach { player ->
                            player.points++
                        }
                    }
                }
                Roles.Commissar -> {
                    if (it.purpose!!.role == Roles.Mafia.name_ru) {
                        it.players.forEach { player ->
                            player.points++
                        }
                    }
                }
                else -> {}
            }
        }
    }

    fun checkWin(): Int {
        val mafiaCount = currentPlayersFlow.value
            .count { !it.isDead && it.role == Roles.Mafia.name_ru }
        val peacefulCount = currentPlayersFlow.value
            .count { !it.isDead && it.role != Roles.Maniac.name_ru && it.role != Roles.Mafia.name_ru }
        val maniacCount = currentPlayersFlow.value
            .count { !it.isDead && it.role == Roles.Maniac.name_ru }

        return if (mafiaCount > 0 && maniacCount >= peacefulCount && maniacCount == 0) {
            // mafia wins
            setPoints(2)
            2
        } else if (mafiaCount == 0 && maniacCount == 0 && peacefulCount > 0) {
            // peaceful wins
            setPoints(1)
            1
        } else if (mafiaCount == 0 && maniacCount > 0 && peacefulCount == 0) {
            // maniac wins
            setPoints(3)
            3
        } else if ((maniacCount == mafiaCount && mafiaCount > 0 && peacefulCount == 0) ||
            (mafiaCount == 0 && maniacCount == 0 && peacefulCount == 0)
        ) {
            // draw
            setPoints(4)
            4
        } else 0
    }

    private fun setPoints(winner: Int) {
        when (winner) {
            1 -> {
                currentPlayersFlow.value
                    .filter { it.role != Roles.Mafia.name_ru && it.role != Roles.Maniac.name_ru }
                    .forEach { player ->
                        if (player.isDead) {
                            player.points += 2
                        } else {
                            player.points += 3
                        }
                    }
            }
            2 -> {
                currentPlayersFlow.value
                    .filter { it.role == Roles.Mafia.name_ru }
                    .forEach { player ->
                        if (player.isDead) {
                            player.points += 3
                        } else {
                            player.points += 4
                        }
                    }
            }
            3 -> {
                currentPlayersFlow.value
                    .filter { it.role == Roles.Maniac.name_ru }
                    .forEach { player ->
                        player.points += 7
                    }
            }
            4 -> {
                val maniacCount = currentPlayersFlow.value
                    .count { it.role == Roles.Maniac.name_ru }
                val mafiaCount = currentPlayersFlow.value
                    .count { it.role == Roles.Mafia.name_ru }

                if (mafiaCount > 0 && mafiaCount == maniacCount) {
                    currentPlayersFlow.value
                        .forEach { player ->
                            if (player.role == Roles.Maniac.name_ru) {
                                Roles.Maniac.players.forEach { maniac ->
                                    maniac.points += 2
                                }
                            } else if (player.role == Roles.Mafia.name_ru) {
                                Roles.Mafia.players.forEach { mafia ->
                                    mafia.points += 1
                                }
                            }
                        }
                }
            }
        }
    }
}