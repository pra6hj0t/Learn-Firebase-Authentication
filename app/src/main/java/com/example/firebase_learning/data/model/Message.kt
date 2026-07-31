package com.example.firebase_learning.data.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",


    val text: String = "",
    val imageUrl: String = "",

    val timestamp: Long = 0L,
    val seen: Boolean = false
)
