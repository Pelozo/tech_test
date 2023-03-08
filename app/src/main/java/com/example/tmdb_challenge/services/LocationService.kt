package com.example.tmdb_challenge.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tmdb_challenge.R
import com.example.tmdb_challenge.usecases.location.UploadCurrentLocationUseCase
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService @Inject constructor(
): Service() {
    @Inject lateinit var uploadCurrentLocationUseCase: UploadCurrentLocationUseCase
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    scope.launch {
                        val success = uploadCurrentLocationUseCase.invoke(it).await()
                        if(success){
                            showNotification()
                        }
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = getString(R.string.popular)
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager: NotificationManager? = ContextCompat.getSystemService(
                application,
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(){
        val notificationBuilder = NotificationCompat.Builder(application, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(getString(R.string.notif_title_uploading_location))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
        val notificationManagerCompat = NotificationManagerCompat.from(application)
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    override fun onBind(intent: Intent?): IBinder? {
        // This service does not provide binding
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        val notificationManagerCompat = NotificationManagerCompat.from(application)
        notificationManagerCompat.cancel(NOTIFICATION_ID)
    }

    companion object{
        private const val CHANNEL_ID = "location"
        private const val NOTIFICATION_ID = 1
    }

}