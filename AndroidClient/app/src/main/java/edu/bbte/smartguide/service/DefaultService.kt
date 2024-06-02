package edu.bbte.smartguide.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DefaultService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var geofenceManager: GeofenceManager
    private lateinit var mediaPlayer: MediaPlayer
    private val TAG = "DefaultService"

    class LocalBinder : Binder() {
        val service: DefaultService
            get() =  DefaultService()
    }

    private val binder: IBinder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        geofenceManager = GeofenceManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val audioUriString = intent?.getStringExtra("AUDIO_URI")

        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
            ACTION_MEDIA_PLAY -> mediaPlay(audioUriString)
            ACTION_MEDIA_STOP -> mediaStop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        Log.d(TAG, "Started")

//        val stopNotificationIntent = Intent(this, DefaultService::class.java).apply {
//            action = ACTION_STOP
//        }
        val stopNotificationIntent = PendingIntent.getService(this,
            0,
            Intent(this, DefaultService::class.java).apply {
                action = ACTION_STOP
            },
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        );

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Smart Audio Guide is active")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .addAction(android.R.drawable.ic_media_pause, "Stop", stopNotificationIntent)
            .build()

        locationClient
            .getLocationUpdates(10000L) //10sec
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude

                Log.d(TAG, "Location: ($lat, $long)")

            }
            .launchIn(serviceScope)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1, notification)
        }

        geofenceManager.addGeofencesFromDb()
    }

    private fun stop() {
        Log.d(TAG, "Ended")

        if (::mediaPlayer.isInitialized) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.release()
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopForeground(STOP_FOREGROUND_DETACH)

        geofenceManager.deregisterGeofence()

        stopSelf()

    }

    private fun mediaPlay(audioUriString: String?) {
        Log.d(TAG, "Media Play")
        if (audioUriString != null) {
            val audioUri = Uri.parse(audioUriString)
            mediaPlayer = MediaPlayer.create(this, audioUri)
            mediaPlayer.start()
        }
    }

    private fun mediaStop() {
        Log.d(TAG, "Media Stop")
        if (::mediaPlayer.isInitialized) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            mediaPlayer.release()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")

        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_MEDIA_PLAY = "ACTION_MEDIA_PLAY"
        const val ACTION_MEDIA_STOP = "ACTION_MEDIA_STOP"
    }
}