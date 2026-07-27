package com.example.firebase_learning.presentation.states

import com.example.firebase_learning.data.model.User

sealed class UserUiState {

    object Idle : UserUiState()

    object Loading : UserUiState()

    data class Success(
        val users: List<User>
    ) : UserUiState()




    data class Error(
        val message: String
    ) : UserUiState()
}