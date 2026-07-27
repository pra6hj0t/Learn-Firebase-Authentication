package com.example.firebase_learning.presentation.states

import com.example.firebase_learning.data.model.User

sealed class CurrentUserUiState {
    object Idle : CurrentUserUiState()
    object Loading : CurrentUserUiState()
    data class Success(
        val user: User
    ) : CurrentUserUiState()

    data class Error(val message: String) : CurrentUserUiState()

}