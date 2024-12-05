package com.tech.indiaekartshoppinguser.presentation.screens.payment.noification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.tech.indiaekartshoppinguser.R
import kotlin.random.Random

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        val permissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if(!permissionState.status.isGranted){
                permissionState.launchPermissionRequest()
            }
        }
    }
}
fun createNotificationChannel(context : Context){
    val name = "india EKart Channel"
    val desc = "test_desc"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel("CHANNEL_ID",name,importance).apply {
        description = desc
    }
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}
fun sendNotification(context: Context){
    val builder = NotificationCompat.Builder(context,"CHANNEL_ID")
        .setSmallIcon(R.drawable.ellipse_1)
        .setContentTitle("Order")
        .setContentText("Order Created Successfully😊")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(Random.nextInt(),builder.build())
}
