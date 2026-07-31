package com.example.firebase_learning.data.repo


import android.util.Log
import com.example.firebase_learning.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore

) {


    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {
                val firebaseUser = auth.currentUser ?: return@addOnSuccessListener
                val newUser = User(
                    uid = firebaseUser.uid,
                    name = name,
                    email = email,
                    profileImage = "",
                    online = true
                )
                saveUser(newUser)

            }
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(
            email,
            password
        )
    }


    fun logout() {
        auth.signOut()

    }

    fun saveUser(user: User): Task<Void> {
        return firestore
            .collection("users")
            .document(user.uid)
            .set(user)

    }


    //for getting only one user
    fun getUser(uid: String): Task<DocumentSnapshot> {
        return firestore
            .collection("users")
            .document(uid)
            .get()
    }


    //for getting all users
    fun getAllUsers(): Task<QuerySnapshot> {
        return firestore
            .collection("users")
            .get()

    }


    fun updateOnlineStatus(
        isOnline: Boolean
    ) {

        Log.d("APP_STATUS", "Updating Firestore : $isOnline")


        val uid = auth.currentUser?.uid ?: return


        val updates = mapOf(
            "online" to isOnline,
            "lastSeen" to System.currentTimeMillis()
        )

        firestore
            .collection("users")
            .document(uid)
            .update(updates)

    }

    fun updateOfflineBeforeLogout(): Task<Void> {

        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("No logged in user")

        return firestore.collection("users")
            .document(uid)
            .update(
                mapOf(
                    "online" to false,
                    "lastSeen" to System.currentTimeMillis()
                )
            )
    }
}