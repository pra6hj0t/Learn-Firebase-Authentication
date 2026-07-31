package com.example.firebase_learning.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {


    fun formatLastSeen(timestamp: Long): String {

        val date = Date(timestamp)

        val now = Calendar.getInstance()
        val lastSeen = Calendar.getInstance().apply {
            time = date
        }

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

        return when {

            now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) == lastSeen.get(Calendar.DAY_OF_YEAR) -> {

                "Last seen today at ${timeFormat.format(date)}"
            }

            now.get(Calendar.YEAR) == lastSeen.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) - lastSeen.get(Calendar.DAY_OF_YEAR) == 1 -> {

                "Last seen yesterday at ${timeFormat.format(date)}"
            }

            else -> {

                "Last seen ${dateFormat.format(date)}"
            }
        }
    }

    fun formatTime(timestamp: Long): String {

        val formatter = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )

        return formatter.format(Date(timestamp))
    }

}