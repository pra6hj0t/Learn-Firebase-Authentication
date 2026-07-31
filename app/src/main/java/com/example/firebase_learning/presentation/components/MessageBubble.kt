package com.example.firebase_learning.presentation.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.firebase_learning.data.model.Message
import com.example.firebase_learning.utils.TimeUtils

@Composable
fun MessageBubble(
    message: Message,
    isMe: Boolean,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement =
            if (isMe) Arrangement.End
            else Arrangement.Start
    ) {

        Surface(
            color =
                if (isMe)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                horizontalAlignment =
                    if (isMe)
                        Alignment.End
                    else
                        Alignment.Start
            )
            {


                if (message.imageUrl.isNotEmpty()) {

                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(220.dp)
                            .padding(8.dp)
                            .clickable {
                                val encodedUrl =
                                    Uri.encode(message.imageUrl)

                                navController.navigate(
                                    "image_viewer/$encodedUrl"
                                )
                            },
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Text(
                        text = message.text,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        )
                    )

                }


                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = TimeUtils.formatTime(message.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (isMe) {

                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))

                        Text(
                            text = if (message.seen) "✓✓" else "✓",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

            }

        }

    }

}