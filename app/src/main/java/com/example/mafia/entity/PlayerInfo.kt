package com.example.mafia.entity

data class PlayerInfo(
    var nickname: String,
    var number: Int,
    var role: String?,
    var isExpose: Boolean,
    var isDead: Boolean,
    var votes: Int,
    var points: Int,
)