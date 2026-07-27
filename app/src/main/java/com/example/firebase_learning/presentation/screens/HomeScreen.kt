package com.example.firebase_learning.presentation.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firebase_learning.navigation.Routes
import com.example.firebase_learning.presentation.states.CurrentUserUiState
import com.example.firebase_learning.presentation.states.UserUiState
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel, navController: NavHostController) {

    Scaffold() { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LaunchedEffect(Unit) {
                viewModel.getAllUsers()
                viewModel.getUser()
            }

            when (val state = viewModel.currentUserUiState) {
                is CurrentUserUiState.Idle -> {

                }

                is CurrentUserUiState.Loading -> {

                }

                is CurrentUserUiState.Success -> {
                    Spacer(Modifier.height(30.dp))
                    Text(text = "Welcome ${state.user.name}")
                }

                is CurrentUserUiState.Error -> {
                    Text(text = state.message)
                }
            }
            Spacer(Modifier.height(30.dp))
            when (val state = viewModel.userUiState) {

                is UserUiState.Idle -> {

                }

                is UserUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UserUiState.Success -> {

                    val users = state.users


                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        items(users) { user ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        navController.navigate("chat_screen/${user.uid}")

                                    }
                                    .padding(16.dp)
                            ) {

                                Text(text = user.name)
                                Text(text = user.email)

                            }
                        }
                    }
                }

                is UserUiState.Error -> {
                    Text(text = state.message)
                }
            }






            Spacer(Modifier.height(30.dp))
            Button(onClick = {
                viewModel.logout()
                navController.navigate(Routes.LOGIN_SCREEN) {
                    popUpTo(Routes.HOME_SCREEN) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Logout")

            }

        }
    }


}