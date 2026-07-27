package com.example.firebase_learning.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firebase_learning.presentation.states.ChatUiState
import com.example.firebase_learning.presentation.states.CurrentUserUiState
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel
import com.example.firebase_learning.presentation.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    receiverId: String?,
    viewModel: AuthViewModel
) {

    var message by remember {
        mutableStateOf("")
    }

    val receiverUid = receiverId ?: return

    val currentUserUid = viewModel.getCurrentUser()?.uid ?: return

    LaunchedEffect(receiverUid) {
        chatViewModel.getUserById(receiverUid)
        chatViewModel.listenForMessages(receiverUid)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {


            when (val state = chatViewModel.currentUserUiState) {
                is CurrentUserUiState.Idle -> {

                }

                is CurrentUserUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is CurrentUserUiState.Success -> {
                    Text(text = "Hi🖐🏻 ${state.user.name}")
                    Spacer(Modifier.height(10.dp))
                    Text(text = if (state.user.online) "Online" else "Offline")
                    Spacer(Modifier.height(30.dp))
                }

                is CurrentUserUiState.Error -> {
                    Text(text = state.message)
                }
            }



            when (val state = chatViewModel.chatUiState) {
                is ChatUiState.Idle -> {

                }

                is ChatUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ChatUiState.Success -> {

                    HorizontalDivider()
                    Spacer(Modifier.height(10.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        items(state.messages) { message ->

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (message.senderId == currentUserUid)
                                    Alignment.CenterEnd else Alignment.CenterStart
                            ) {
                                Text(
                                    text = message.text
                                )
                                Spacer(Modifier.height(20.dp))
                            }

                        }
                    }
                }

                is ChatUiState.Error -> {
                    Text(text = state.message)
                }
            }


            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(30.dp))

            OutlinedTextField(
                value = message,
                onValueChange = {
                    message = it
                }
            )

            Button(
                onClick = {

                    if (message.isNotBlank()) {
                        chatViewModel.sendMessage(
                            receiverId = receiverUid,
                            text = message
                        )
                        message = ""
                    }


                }
            ) {

                Text("Send")

            }
        }
    }


}