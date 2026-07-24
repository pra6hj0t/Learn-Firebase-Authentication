package com.example.firebase_learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase_learning.navigation.NavigationFile
import com.example.firebase_learning.presentation.screens.LoginScreen
import com.example.firebase_learning.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: AuthViewModel = hiltViewModel()
            NavigationFile(viewModel)
        }
    }
}

