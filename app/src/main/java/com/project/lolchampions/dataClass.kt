package com.project.lolchampions

data class Character(
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val lore: String,
    val tags: List<String>,
    val stats: Stats,
    val icon: String
)

data class Stats(
    val hp: Int,
    val mp: Int,
    val movespeed: Int,
    val armor: Int,
    val spellblock: Double,
    val attackdamage: Int
)