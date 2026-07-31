package com.example.firebase_learning.presentation.screens

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.firebase_learning.presentation.states.ImageUploadState
import com.example.firebase_learning.presentation.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewScreen(
    navController: NavHostController, receiverId: String?, imageUri: String?,
    chatViewModel: ChatViewModel
) {

    val uri = remember(imageUri) {
        Uri.parse(imageUri)
    }

    val uploadState =
        chatViewModel.imageUploadState


    LaunchedEffect(uploadState) {

        if (uploadState is ImageUploadState.Success) {

            chatViewModel.resetImageUploadState()

            navController.popBackStack()

        }

    }
    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Text("Preview")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )

                    }

                }

            )

        }
    ) { padding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


            AsyncImage(

                model = uri,

                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                contentScale = ContentScale.Fit

            )


            Button(
                onClick = {
                    chatViewModel.sendImageMessage(
                        receiverId!!,
                        uri
                    )


                }, enabled = uploadState !is ImageUploadState.Uploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {


                if (uploadState is ImageUploadState.Uploading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("Uploading...")

                } else {

                    Text("Send")

                }

            }


            if (uploadState is ImageUploadState.Error) {

                Text(
                    text = uploadState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    }


}