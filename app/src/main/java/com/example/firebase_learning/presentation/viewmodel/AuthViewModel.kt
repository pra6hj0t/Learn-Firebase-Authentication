package com.example.firebase_learning.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_learning.data.repo.AuthRepo
import com.example.firebase_learning.presentation.AuthUiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepo
) : ViewModel() {


    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()

    }


    fun isUserLoggedIn(): Boolean {
        return repo.getCurrentUser() != null
    }

    var uiState by mutableStateOf<AuthUiState>(
        AuthUiState.Idle
    )
        private set


    fun register(email: String, password: String) {

        uiState = AuthUiState.Loading

        repo.register(email, password)
            .addOnSuccessListener {
                uiState = AuthUiState.Success
            }
            .addOnFailureListener {
                uiState = AuthUiState.Error(it.message ?: "Unknown Error")
            }
    }

    fun login(email: String, password: String) {
        uiState = AuthUiState.Loading

        repo.login(email = email, password = password)
            .addOnSuccessListener {
                uiState = AuthUiState.Success
            }
            .addOnFailureListener {
                uiState = AuthUiState.Error(it.message ?: "Unknown Error")
            }
    }

    fun logout() {
        repo.logout()
        uiState = AuthUiState.Idle

    }


}