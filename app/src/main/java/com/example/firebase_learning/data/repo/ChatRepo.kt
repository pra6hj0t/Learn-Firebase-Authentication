package com.example.firebase_learning.data.repo

import com.example.firebase_learning.data.model.Message
import com.example.firebase_learning.utils.ChatUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore

) {

    fun sendMessage(
        text: String,
        receiverId: String
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
            text = text,
            timestamp = System.currentTimeMillis(),
            isSeen = false
        )

        return messageRef.set(message)

    }


    fun listenForMessages(
        receiverId: String,
        onMessageChanged: (List<Message>) -> Unit
    ) {
        val senderId = auth.currentUser?.uid ?: return

        val chatId = ChatUtils.generateChatId(senderId, receiverId)

        firestore
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

}