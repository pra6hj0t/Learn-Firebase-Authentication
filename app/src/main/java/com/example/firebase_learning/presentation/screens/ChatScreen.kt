package com.example.firebase_learning.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firebase_learning.presentation.components.ChatTopBar
import com.example.firebase_learning.presentation.components.MessageBubble
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

    DisposableEffect(Unit) {

        onDispose {
            chatViewModel.stopListening()
        }
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let { imageUri ->

            val encodeUri = Uri.encode(imageUri.toString())

            navController.navigate(
                "image_preview_screen/$receiverUid/$encodeUri"
            )

        }

    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            when (val state = chatViewModel.currentUserUiState) {
                is CurrentUserUiState.Success -> {
                    ChatTopBar(
                        user = state.user,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                else -> {}

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {


            when (val state = chatViewModel.chatUiState) {
                is ChatUiState.Idle -> {

                }

                is ChatUiState.Loading -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        CircularProgressIndicator()
                    }

                }

                is ChatUiState.Success -> {


                    val listState = rememberLazyListState()

                    LaunchedEffect(state.messages.size) {
                        if (state.messages.isNotEmpty()) {
                            listState.animateScrollToItem(
                                state.messages.lastIndex
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp),
                        state = listState,


                        ) {
                        items(
                            state.messages
                        ) { message ->
                            MessageBubble(
                                message = message,
                                isMe = message.senderId == currentUserUid,
                                navController = navController
                            )

                        }
                    }
                }

                is ChatUiState.Error -> {
                    Text(text = state.message)
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    imagePickerLauncher.launch("image/*")

                }) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Gallery"
                    )
                }
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    placeholder = {
                        Text(text = "Type your message")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(25.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                IconButton(onClick = {
                    if (message.isNotBlank()) {
                        chatViewModel.sendMessage(
                            receiverId = receiverUid,
                            text = message
                        )
                        message = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Send,
                        contentDescription = "Send"
                    )
                }


            }


        }
    }


}