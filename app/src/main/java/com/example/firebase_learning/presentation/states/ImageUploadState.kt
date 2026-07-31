package com.example.firebase_learning.presentation.states

sealed interface ImageUploadState {

    data object Idle : ImageUploadState

    data object Uploading : ImageUploadState

    data object Success : ImageUploadState

    data class Error(
        val message: String
    ) : ImageUploadState

}