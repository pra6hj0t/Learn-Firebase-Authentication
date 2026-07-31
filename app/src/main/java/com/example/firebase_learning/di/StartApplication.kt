package com.example.firebase_learning.di

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.firebase_learning.data.repo.AuthRepo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.lifecycle.ProcessLifecycleOwner


@HiltAndroidApp
class StartApplication : Application() {

    @Inject
    lateinit var authRepo: AuthRepo

    private val appObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)

            Log.d("APP_STATUS", "App Started")
            authRepo.updateOnlineStatus(true)
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)

            Log.d("APP_STATUS", "App Stopped")
            authRepo.updateOnlineStatus(false)
        }

    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner
            .get()
            .lifecycle
            .addObserver(appObserver)
    }
}