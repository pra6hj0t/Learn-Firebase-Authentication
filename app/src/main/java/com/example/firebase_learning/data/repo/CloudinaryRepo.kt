package com.example.firebase_learning.data.repo

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class CloudinaryRepo @Inject constructor(
    @ApplicationContext
    private val context: Context

) {

    private companion object {
        const val CLOUD_NAME = "fxmee8qt"
        const val UPLOAD_PRESET = "chat_unsigned"
    }

    private val uploadUrl =
        "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"


    private val client = OkHttpClient()

    suspend fun uploadImage(imageUri: Uri): Result<String> {

        val inputStream =
            context.contentResolver.openInputStream(imageUri)
                ?: return Result.failure(Exception("Unable to open image"))

        val imageBytes = inputStream.readBytes()
        inputStream.close()

        val tempFile = File.createTempFile(
            "upload",
            ".jpg",
            context.cacheDir
        )

        tempFile.writeBytes(imageBytes)


        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                tempFile.name,
                tempFile
                    .asRequestBody("image/*".toMediaType())
            )
            .addFormDataPart(
                "upload_preset",
                UPLOAD_PRESET
            )
            .build()


        val request = Request.Builder()
            .url(uploadUrl)
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Upload Failed")
                    )
                }

                val responseBody = response.body?.string()
                    ?: return@withContext Result.failure(
                        Exception("Empty Response")
                    )

                val json = JSONObject(responseBody)

                val imageUrl = json.getString("secure_url")

                Result.success(imageUrl)
            } catch (e: Exception) {

                Result.failure(e)

            }
        }


    }
}