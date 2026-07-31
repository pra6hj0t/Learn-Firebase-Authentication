package com.example.firebase_learning.presentation.states

sealed class AuthUiState {

    object Idle : AuthUiState()

    object Loading : AuthUiState()

    object Success : AuthUiState()

    object LoggedOut : AuthUiState()

    data class Error(val message: String) : AuthUiState()



}