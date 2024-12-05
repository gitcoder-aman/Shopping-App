package com.tech.indiaekartshoppinguser.data

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.common.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class PushNotification @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var accessToken: String? = null

    init {
        coroutineScope.launch {
            updateAccessToken()
        }
    }


    fun sendNotification(
        title: String,
        message: String
    ) {
        coroutineScope.launch {
            try {
                // Ensure access token is available
                if (accessToken == null) {
                    updateAccessToken()
                }

                // Fetch user tokens and send notifications
                val token = getUserToken()
                Log.d("@noti", "sendNotification: $token")
                setUpNotification(token, title, message)
            } catch (e: Exception) {
                e.printStackTrace() // Log the exception
            }
        }
    }

    private suspend fun getUserToken(): String = withContext(Dispatchers.IO) {
        val snap = firebaseFireStore.collection(USER_TOKEN).document(firebaseAuth.currentUser?.uid.toString()).get().await()
        snap.getString("token").toString()
    }

    private suspend fun updateAccessToken() = withContext(Dispatchers.IO) {
        try {
            val stream = context.resources.openRawResource(R.raw.firebase_key)
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped("https://www.googleapis.com/auth/firebase.messaging")
            credentials.refresh()
            accessToken = credentials.accessToken.tokenValue
            Log.d("@noti", "updateAccessToken: $accessToken")
        } catch (e: Exception) {
            e.printStackTrace() // Handle errors in updating access token
            Log.d("@noti", "updateAccessToken Exception: ${e.message}")
        }
    }

    private suspend fun setUpNotification(
        token: String,
        title: String,
        message: String
    ) {

        Log.d("@noti", "accessToken: $accessToken")
        try {
            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", token)
                    put("notification", JSONObject().apply {
                        put("title", title)
                        put("body", message)
                    })
                })
            }
            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = okhttp3.Request.Builder()
                .header("Authorization", "Bearer $accessToken")
                .url("https://fcm.googleapis.com/v1/projects/chatappcompose-1b77d/messages:send")
                .post(body)
                .build()

            val response = withContext(Dispatchers.IO) {
                okhttp3.OkHttpClient().newCall(request).execute()
            }

            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                Log.d("@noti", "setUpNotification errorBody: ${response.body?.string()}")
                throw Exception("Error sending notification: $errorBody")
            }else{
                Log.d("@noti", "setUpNotification Response: ${response.body?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log individual notification sending failures
            Log.d("@noti", "setUpNotification Exception: ${e.message}")
        }
    }
}
