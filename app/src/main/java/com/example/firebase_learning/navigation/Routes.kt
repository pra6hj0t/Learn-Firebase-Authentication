package com.example.firebase_learning.navigation

object Routes {

    const val REGISTER_SCREEN = "register_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val HOME_SCREEN = "home_screen"

    const val CHAT_SCREEN = "chat_screen"

    const val CHAT_SCREEN_WITH_ARG = "chat_screen/{receiverId}"

    const val IMAGE_PREVIEW_SCREEN = "image_preview_screen"
    const val IMAGE_PREVIEW_SCREEN_WITH_ARG =
        "image_preview_screen/{receiverId}/{imageUri}"

    const val IMAGE_VIEWER = "image_viewer"

    const val IMAGE_VIEWER_WITH_ARG =
        "image_viewer/{imageUrl}"

}