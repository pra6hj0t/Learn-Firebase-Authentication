package com.example.firebase_learning.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firebase_learning.data.model.User
import com.example.firebase_learning.data.repo.AuthRepo
import com.example.firebase_learning.data.repo.ChatRepo
import com.example.firebase_learning.presentation.states.ChatUiState
import com.example.firebase_learning.presentation.states.CurrentUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.jvm.java


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepo,
    private val auth: AuthRepo
) : ViewModel() {


    var currentUserUiState by mutableStateOf<CurrentUserUiState>(
        CurrentUserUiState.Idle
    )
        private set

    var chatUiState by mutableStateOf<ChatUiState>(
        ChatUiState.Idle
    )
        private set


    fun sendMessage(receiverId: String, text: String) {
        repo.sendMessage(text = text, receiverId = receiverId)

    }

    fun getUserById(uid: String?) {

        if (uid == null) {
            currentUserUiState = CurrentUserUiState.Error("User not found")
            return
        }
        currentUserUiState = CurrentUserUiState.Loading

        auth.getUser(uid)
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    currentUserUiState = CurrentUserUiState.Success(user)
                } else {
                    currentUserUiState = CurrentUserUiState.Error("User not found")
                }
            }
            .addOnFailureListener {
                currentUserUiState = CurrentUserUiState.Error(it.message ?: "Unknown Error")

            }


    }


    fun listenForMessages(receiverId: String) {

        chatUiState = ChatUiState.Loading
        repo.listenForMessages(receiverId) { messages ->
            chatUiState = ChatUiState.Success(messages)
        }
    }


}