package edu.bbte.smartguide.service.tracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import edu.bbte.smartguide.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val binder = LocalBinder()
    inner class LocalBinder: Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Smart Audio Guide is active")
            .setContentText("Location: ?")
            .setSmallIcon(R.drawable.ic_launcher_foreground )
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        locationClient
            .getLocationUpdates(10000L) //10sec
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude

                Log.d("LocationService", "Location: ($lat, $long)")

//                locationCallback?.onLocationUpdated(lat, long)

                val updateNotification = notification.setContentText("Location: ($lat, $long)")
                notificationManager.notify(1, updateNotification.build())
            }
            .launchIn(serviceScope)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1, notification.build(),)
        }    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}