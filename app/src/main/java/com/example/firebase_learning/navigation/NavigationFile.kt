package com.example.firebase_learning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebase_learning.presentation.screens.ChatScreen
import com.example.firebase_learning.presentation.screens.HomeScreen
import com.example.firebase_learning.presentation.screens.ImagePreviewScreen
import com.example.firebase_learning.presentation.screens.ImageViewerScreen
import com.example.firebase_learning.presentation.screens.LoginScreen
import com.example.firebase_learning.presentation.screens.RegisterScreen
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel
import com.example.firebase_learning.presentation.viewmodel.ChatViewModel

@Composable
fun NavigationFile(viewModel: AuthViewModel, chatViewModel: ChatViewModel) {

    val navController = rememberNavController()

    val startDestination = if (viewModel.isUserLoggedIn()) {
        Routes.HOME_SCREEN
    } else {
        Routes.REGISTER_SCREEN
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.REGISTER_SCREEN) {
            RegisterScreen(viewModel, navController)
        }

        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(viewModel, navController)
        }

        composable(Routes.HOME_SCREEN) {
            HomeScreen(viewModel, navController)
        }
        composable(route = Routes.CHAT_SCREEN_WITH_ARG) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId")
            // Use receiverId as needed
            ChatScreen(chatViewModel, navController, receiverId, viewModel)

        }

        composable(route = Routes.IMAGE_PREVIEW_SCREEN_WITH_ARG) { backStackEntry ->

            val receiverId = backStackEntry.arguments?.getString("receiverId")

            val imageUri = backStackEntry.arguments?.getString("imageUri")
            // Use receiverId and imageUri as needed
            ImagePreviewScreen(
                navController = navController,
                receiverId = receiverId ?: "",
                imageUri = imageUri ?: "",
                chatViewModel = chatViewModel
            )
        }

        composable(
            route = Routes.IMAGE_VIEWER_WITH_ARG
        ) { backStackEntry ->

            val imageUrl =
                backStackEntry.arguments?.getString("imageUrl") ?: ""

            ImageViewerScreen(
                navController = navController,
                imageUrl = imageUrl
            )

        }


    }

}