package com.example.mafia.entity

enum class NightResults(var players: MutableList<Player>) {
    LastDeath(mutableListOf()),
    LastSavedByWhore(mutableListOf()),
    LastSavedByDoctor(mutableListOf()),
    LastDeathByWhore(mutableListOf())
}