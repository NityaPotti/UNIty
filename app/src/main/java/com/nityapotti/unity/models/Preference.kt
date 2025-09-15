package com.nityapotti.unity.models

data class Preference (
    val id: String = "",
    val visible: Boolean = false,
    val name: String = "",
    val gender: String = "",
    val temperature: String = "",
    val bedtime: String = "",
    val cleaniness: Int = 0,
    val oncampus: String = "",
    val location: String = "",
    val llc: String = "",
    val maxrent: Int = 0,
    val bio: String = "",

)