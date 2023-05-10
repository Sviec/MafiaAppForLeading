package com.example.mafia.entity

enum class NightResults(var playerInfos: MutableList<PlayerInfo>) {
    LastDeath(mutableListOf()),
    LastSavedByWhore(mutableListOf()),
    LastSavedByDoctor(mutableListOf()),
    LastDeathByWhore(mutableListOf())
}