package com.nityapotti.unity.models

import com.google.firebase.Timestamp

data class Message (
    val sender: String = "",
    val receiver: String = "",
    val text: String = "",
    val viewed: Boolean = false,
    val timestamp: Timestamp? = null
)