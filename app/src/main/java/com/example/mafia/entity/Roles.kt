package com.example.mafia.entity

enum class Roles (
    val name_ru: String,
    val priority: Int,
    var players: List<Player> = emptyList(),
    var purpose: Player? = null
) {
    NoRule("Нет роли", 100),
    Mafia("Мафия", 1),
    Maniac("Маньяк", 2),
    Doctor("Доктор", 3),
    Whore("Красотка", 4),
    Commissar("Комиссар", 5),
    Peaceful("Мирный", 11)
}