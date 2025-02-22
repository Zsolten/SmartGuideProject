package edu.bbte.smartguide.ui.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import edu.bbte.smartguide.model.Regions
import edu.bbte.smartguide.retrofit.RetrofitInstance
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "location",
            "Background Location",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}