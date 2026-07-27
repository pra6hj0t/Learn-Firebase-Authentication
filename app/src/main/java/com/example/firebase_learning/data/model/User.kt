package com.example.firebase_learning.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImage: String = "",
    val online: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
