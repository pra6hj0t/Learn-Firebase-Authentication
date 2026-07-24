package com.example.firebase_learning.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.firebase_learning.navigation.Routes
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel, navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val user = viewModel.getCurrentUser()




        Text(
            "Email : ${user?.email}"
        )

        Spacer(Modifier.height(10.dp))
        Text(
            "Welcome Home 🎉"
        )
        Spacer(Modifier.height(10.dp))
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