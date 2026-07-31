        package com.example.firebase_learning.presentation.viewmodel

        import androidx.compose.runtime.getValue
        import androidx.compose.runtime.mutableStateOf
        import androidx.compose.runtime.setValue
        import androidx.lifecycle.ViewModel
        import com.example.firebase_learning.data.model.User
        import com.example.firebase_learning.data.repo.AuthRepo
        import com.example.firebase_learning.presentation.states.AuthUiState
        import com.example.firebase_learning.presentation.states.CurrentUserUiState
        import com.example.firebase_learning.presentation.states.UserUiState
        import com.google.firebase.auth.FirebaseUser
        import dagger.hilt.android.lifecycle.HiltViewModel
        import javax.inject.Inject


        @HiltViewModel
        class AuthViewModel @Inject constructor(
            private val repo: AuthRepo
        ) : ViewModel() {


            fun getCurrentUser(): FirebaseUser? {
                return repo.getCurrentUser()

            }


            fun isUserLoggedIn(): Boolean {
                return repo.getCurrentUser() != null
            }

            var uiState by mutableStateOf<AuthUiState>(
                AuthUiState.Idle
            )
                private set

            var userUiState by mutableStateOf<UserUiState>(
                UserUiState.Idle
            )
                private set

            var currentUserUiState by mutableStateOf<CurrentUserUiState>(
                CurrentUserUiState.Idle
            )
                private set


            fun register(name: String, email: String, password: String) {

                uiState = AuthUiState.Loading

                repo.register(name, email, password)
                    .addOnSuccessListener {
                        uiState = AuthUiState.Success

                    }
                    .addOnFailureListener {
                        uiState = AuthUiState.Error(it.message ?: "Unknown Error")
                    }
            }

            fun login(email: String, password: String) {
                uiState = AuthUiState.Loading

                repo.login(email = email, password = password)
                    .addOnSuccessListener {
                        repo.updateOnlineStatus(true)
                        uiState = AuthUiState.Success
                    }
                    .addOnFailureListener {
                        uiState = AuthUiState.Error(it.message ?: "Unknown Error")
                    }
            }

            fun logout() {

                repo.updateOfflineBeforeLogout()
                    .addOnSuccessListener {
                        repo.logout()
                        uiState = AuthUiState.LoggedOut
                    }
                    .addOnFailureListener {
                        repo.logout()
                        uiState = AuthUiState.LoggedOut
                    }


            }


            fun getUser() {

                val uid = repo.getCurrentUser()?.uid

                if (uid == null) {
                    userUiState = UserUiState.Error("User not found")
                    return

                }
                currentUserUiState = CurrentUserUiState.Loading

                repo.getUser(uid)
                    .addOnSuccessListener { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        if (user != null) {
                            currentUserUiState = CurrentUserUiState.Success(user)
                        } else {
                            currentUserUiState = CurrentUserUiState.Error("User not found")
                        }
                    }
                    .addOnFailureListener {
                        currentUserUiState = CurrentUserUiState.Error(it.message ?: "Unknown Error")

                    }


            }


            fun getAllUsers() {
                userUiState = UserUiState.Loading
                repo.getAllUsers()
                    .addOnSuccessListener { querySnapshot ->

                        val userList = mutableListOf<User>()

                        val currentUid = repo.getCurrentUser()?.uid

                        for (document in querySnapshot.documents) {
                            val user = document.toObject(User::class.java)

                            if (user != null && user.uid != currentUid) {
                                userList.add(user)
                            }
                        }
                        userUiState = UserUiState.Success(userList)


                    }
                    .addOnFailureListener {
                        userUiState = UserUiState.Error(it.message ?: "Unknown Error")
                    }
            }


        }