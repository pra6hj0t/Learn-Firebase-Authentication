package com.example.firebase_learning.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.firebase_learning.data.model.User
import com.example.firebase_learning.utils.TimeUtils.formatLastSeen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    user: User,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = user.name
                )
                Text(
                    text = if (user.online)
                        "Online"
                    else
                        formatLastSeen(user.lastSeen),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (user.online)
                        Color(0xFF4CAF50)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}