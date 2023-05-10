package com.example.mafia.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mafia.entity.NightResults
import com.example.mafia.entity.PlayerInfo
import com.example.mafia.entity.Roles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    var nightNumber = 0
    private val currentPlayersList = mutableListOf<PlayerInfo>()

    private val _currentPlayersFlow = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val currentPlayersFlow = _currentPlayersFlow.asStateFlow()

    fun addPlayer(playerInfo: PlayerInfo) {
        currentPlayersList.add(playerInfo)
        _currentPlayersFlow.value = currentPlayersList.toList()
    }

    fun deletePlayer(playerInfo: PlayerInfo) {
        currentPlayersList.remove(playerInfo)
        _currentPlayersFlow.value = currentPlayersList.toList()
    }

    fun setRoles() {
        Roles.values().forEach {
            it.playerInfos = currentPlayersFlow.value.filter { player ->
                player.role == it.name_ru
            }
        }
    }

    fun votingResults() {
        val exposePlayers = currentPlayersFlow.value.filter { it.isExpose }
        if ((nightNumber == 0 && exposePlayers.size == 1) ||
            exposePlayers.isEmpty()
        ) {
            currentPlayersFlow.value.filter {
                it.isExpose
            }.forEach { it.isExpose = false }
        } else {
            val maxVotes = currentPlayersFlow.value.maxBy { it.votes }.votes
            currentPlayersFlow.value.forEach {
                if (it.votes != maxVotes) it.isExpose = false
            }
        }
    }

    fun clearVotingInfo() {
        currentPlayersFlow.value.forEach {
            it.votes = 0
            it.isExpose = false
        }
    }

    fun clearVotes() {
        currentPlayersFlow.value.forEach {
            it.votes = 0
        }
    }

    fun nightResults() {
        Roles.values().filter { it.purpose != null }.forEach {
            when (it) {
                Roles.Mafia -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        it.purpose!!.isDead = true
                        NightResults.LastDeath.playerInfos.add(it.purpose!!)
                        Roles.Whore.purpose!!.isDead = true
                        NightResults.LastDeathByWhore.playerInfos.add(Roles.Whore.purpose!!)
                    } else if (it.purpose != Roles.Whore.purpose) {
                        it.purpose!!.isDead = true
                        NightResults.LastDeath.playerInfos.add(it.purpose!!)
                    } else {
                        NightResults.LastSavedByWhore.playerInfos.add(it.purpose!!)
                    }
                }
                Roles.Maniac -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        it.purpose!!.isDead = true
                        NightResults.LastDeath.playerInfos.add(it.purpose!!)
                        Roles.Whore.purpose!!.isDead = true
                        NightResults.LastDeathByWhore.playerInfos.add(Roles.Whore.purpose!!)
                    } else if (it.purpose != Roles.Whore.purpose) {
                        it.purpose!!.isDead = true
                        NightResults.LastDeath.playerInfos.add(it.purpose!!)
                    } else {
                        NightResults.LastSavedByWhore.playerInfos.add(it.purpose!!)
                    }
                }
                Roles.Doctor -> {
                    if (it.purpose!!.role == Roles.Whore.name_ru) {
                        if (it.purpose!!.isDead) {
                            NightResults.LastDeath.playerInfos.remove(it.purpose!!)
                            NightResults.LastDeathByWhore.playerInfos.remove(Roles.Whore.purpose!!)
                            NightResults.LastSavedByDoctor.playerInfos.add(it.purpose!!)
                            NightResults.LastSavedByDoctor.playerInfos.add(Roles.Whore.purpose!!)
                            it.purpose!!.isDead = false
                            Roles.Whore.purpose!!.isDead = false
                        }
                    } else {
                        if (it.purpose!!.isDead) {
                            if (NightResults.LastDeath.playerInfos.contains(it.purpose!!))
                                NightResults.LastDeath.playerInfos.remove(it.purpose!!)
                            else NightResults.LastDeathByWhore.playerInfos.remove(it.purpose!!)
                            NightResults.LastSavedByDoctor.playerInfos.add(it.purpose!!)
                            it.purpose!!.isDead = false
                        }
                    }
                    if ((it.purpose == Roles.Mafia.purpose || it.purpose == Roles.Maniac.purpose) &&
                        it.purpose!!.role != Roles.Mafia.name_ru ||
                        it.purpose!!.role != Roles.Maniac.name_ru
                    ) {
                        it.playerInfos.forEach { player ->
                            player.points++
                        }
                    }
                }
                Roles.Whore -> {
                    if ((it.purpose == Roles.Mafia.purpose || it.purpose == Roles.Maniac.purpose) &&
                        it.playerInfos.count { player -> !player.isDead } > 0
                    ) {
                        it.playerInfos.forEach { player ->
                            player.points++
                        }
                    } else if (it.playerInfos.count { player -> player.isDead } > 0 &&
                        (it.purpose!!.role == Roles.Mafia.name_ru ||
                                it.purpose!!.role == Roles.Maniac.name_ru)
                    ) {
                        it.playerInfos.forEach { player ->
                            player.points++
                        }
                    }
                }
                Roles.Commissar -> {
                    if (it.purpose!!.role == Roles.Mafia.name_ru) {
                        it.playerInfos.forEach { player ->
                            player.points++
                        }
                    }
                }
                else -> {}
            }
        }
    }

    fun printNightResults(): String {
        var results = ""
        if (NightResults.LastDeath.playerInfos.isNotEmpty())
            results += "Убиты игроки ${NightResults.LastDeath.playerInfos.joinToString(", ") {
                it.number.toString()
            }}\n"
        if (NightResults.LastDeathByWhore.playerInfos.isNotEmpty())
            results += "Красотка забрала ${NightResults.LastDeathByWhore.playerInfos.joinToString(", ") {
                it.number.toString()
            }}\n"
        if (NightResults.LastSavedByDoctor.playerInfos.isNotEmpty())
            results += "Доктор спас ${NightResults.LastSavedByDoctor.playerInfos.joinToString(", ") {
                it.number.toString()
            }}\n"
        if (NightResults.LastSavedByWhore.playerInfos.isNotEmpty())
            results += "Красотка спасла ${NightResults.LastSavedByWhore.playerInfos.joinToString(", ") {
                it.number.toString()
            }}\n"
        return results
    }

    fun checkWin(): Int {
        val mafiaCount = currentPlayersFlow.value
            .count { !it.isDead && it.role == Roles.Mafia.name_ru }
        val peacefulCount = currentPlayersFlow.value
            .count { !it.isDead && it.role != Roles.Maniac.name_ru && it.role != Roles.Mafia.name_ru }
        val maniacCount = currentPlayersFlow.value
            .count { !it.isDead && it.role == Roles.Maniac.name_ru }

        return if (mafiaCount > 0 && mafiaCount >= peacefulCount && maniacCount == 0) {
            // mafia wins
            setPoints(2)
            2
        } else if (mafiaCount == 0 && maniacCount == 0 && peacefulCount > 0) {
            // peaceful wins
            setPoints(1)
            1
        } else if (mafiaCount == 0 && maniacCount > 0 && maniacCount >= peacefulCount) {
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
                    currentPlayersFlow.value.forEach { player ->
                            if (player.role == Roles.Maniac.name_ru) {
                                Roles.Maniac.playerInfos.forEach { maniac ->
                                    maniac.points += 2
                                }
                            } else if (player.role == Roles.Mafia.name_ru) {
                                Roles.Mafia.playerInfos.forEach { mafia ->
                                    mafia.points += 1
                                }
                            }
                        }
                }
            }
        }
    }
}