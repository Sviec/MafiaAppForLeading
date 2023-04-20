package com.example.mafia.entity

data class Player(
    var nickname: String,
    var number: Int,
    var role: String?,
    var isExpose: Boolean,
    var isDead: Boolean,
    var votes: Int
)