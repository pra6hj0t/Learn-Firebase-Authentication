package com.example.firebase_learning.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.firebase_learning.navigation.Routes
import com.example.firebase_learning.presentation.AuthUiState
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel


@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Register Screen", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                placeholder = { Text(text = "Enter Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )

            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Enter Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()

            )
            Spacer(modifier = Modifier.height(10.dp))


            Button(
                onClick = {

                    viewModel.register(email = email, password = password)
                    email = ""
                    password = ""


                },
                enabled = email.isNotBlank() && password.isNotBlank() && password.length >= 6
            ) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.height(10.dp))



            when (val uiState = viewModel.uiState) {
                is AuthUiState.Idle -> {

                }

                is AuthUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthUiState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Routes.LOGIN_SCREEN) {
                            popUpTo(Routes.REGISTER_SCREEN) {
                                inclusive = true
                            }
                        }
                    }
                    Text("Registration Successful ✅")
                }

                is AuthUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }

        }
    }
}