package com.example.firebase_learning.presentation.states

import com.example.firebase_learning.data.model.Message

sealed interface ChatUiState {

    object Idle : ChatUiState

    object Loading : ChatUiState

    data class Success(
        val messages: List<Message>
    ) : ChatUiState

    data class Error(
        val message: String
    ) : ChatUiState

}