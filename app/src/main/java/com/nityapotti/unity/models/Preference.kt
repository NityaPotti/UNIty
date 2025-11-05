package com.nityapotti.unity.models

data class Preference (
    val id: String = "",
    val visible: Boolean = false,
    val name: String = "",
    val gender: String = "",
    val extroversion: String = "",
    val sociability: String = "",
    val major: String ="",
    val year: String = "",
    val noiselevel: Int = 0,
    val guests: String = "",
    val temperature: String = "",
    val bedtime: String = "",
    val wakeup: String = "",
    val cleanliness: Int = 0,
    val oncampus: String = "",
    val location: String = "",
    val llc: String = "",
    val leaselength: String = "",
    val roommates: String = "",
    val maxrent: Int = 0,
    val bio: String = "",
    val sharing: String = "",
    val dealbreakers: List<String> = emptyList(),
    val misc: List<String> = emptyList(),
    val tags: List<String> = emptyList()

    )