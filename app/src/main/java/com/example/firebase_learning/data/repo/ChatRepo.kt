package com.example.firebase_learning.data.repo

import android.net.Uri
import android.util.Log
import com.example.firebase_learning.data.model.Message
import com.example.firebase_learning.utils.ChatUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudinaryRepo: CloudinaryRepo

) {

    fun sendMessage(
        text: String,
        receiverId: String,
    ): Task<Void> {

        val senderId =
            auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")


        val chatId = ChatUtils.generateChatId(senderId, receiverId)

        val messageRef = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .document()

        val message = Message(
            messageId = messageRef.id,
            senderId = senderId,
            receiverId = receiverId,

            imageUrl = "",
            text = text,

            timestamp = System.currentTimeMillis(),
            seen = false
        )


        return messageRef.set(message)

    }


    private var messageListener: ListenerRegistration? = null

    fun stopListening() {
        Log.d("LISTENER", "Listener Removed")
        messageListener?.remove()
        messageListener = null
    }


    fun listenForMessages(
        receiverId: String,
        onMessageChanged: (List<Message>) -> Unit
    ) {
        val senderId = auth.currentUser?.uid ?: return

        val chatId = ChatUtils.generateChatId(senderId, receiverId)

        messageListener?.remove()
        messageListener = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val documents = snapshot?.documents ?: return@addSnapshotListener

                val messageList = mutableListOf<Message>()

                for (document in documents) {
                    val message = document.toObject(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }

                }
                onMessageChanged(messageList)


            }

    }

    fun markMessageAsSeen(receiverId: String) {
        val currentUserUid = auth.currentUser?.uid ?: return

        Log.d("SEEN", "Current User = $currentUserUid")
        Log.d("SEEN", "Receiver = $receiverId")

        val chatId = ChatUtils.generateChatId(currentUserUid, receiverId)

        firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .whereEqualTo("senderId", receiverId)
            .whereEqualTo("receiverId", currentUserUid)
            .whereEqualTo("seen", false)
            .get()
            .addOnSuccessListener { querySnapshot ->

                Log.d("SEEN", "Documents found = ${querySnapshot.size()}")
                for (document in querySnapshot.documents) {
                    Log.d("SEEN", "Updating ${document.id}")

                    document.reference.update("seen", true)
                }
            }

    }


    suspend fun sendImageMessage(
        receiverId: String,
        imageUri: Uri
    ): Result<Unit> {

        val uploadResult =
            cloudinaryRepo.uploadImage(imageUri)


        if (uploadResult.isFailure) {
            return Result.failure(
                uploadResult.exceptionOrNull()
                    ?: Exception("Upload failed")
            )
        }

        val imageUrl = uploadResult.getOrThrow()


        val senderId =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("User not authenticated")
                )


        val chatId =
            ChatUtils.generateChatId(
                senderId,
                receiverId
            )

        val messageRef =
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .document()

        val message = Message(
            messageId = messageRef.id,
            senderId = senderId,
            receiverId = receiverId,
            text = "",
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis(),
            seen = false
        )

        return try {

            messageRef.set(message).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}