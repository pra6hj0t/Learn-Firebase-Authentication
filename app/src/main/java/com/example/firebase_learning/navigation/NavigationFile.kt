package com.example.firebase_learning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase_learning.presentation.screens.HomeScreen
import com.example.firebase_learning.presentation.screens.LoginScreen
import com.example.firebase_learning.presentation.screens.RegisterScreen
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationFile(viewModel: AuthViewModel) {

    val navController = rememberNavController()

    val startDestination = if (viewModel.isUserLoggedIn()) {
        Routes.HOME_SCREEN
    } else {
        Routes.LOGIN_SCREEN
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(viewModel, navController)
        }

        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(viewModel,navController)
        }

        composable(Routes.HOME_SCREEN) {
            HomeScreen(viewModel,navController)
        }


    }

}