package edu.bbte.smartguide.ui.geofence

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import edu.bbte.smartguide.R
import edu.bbte.smartguide.model.Locations
import edu.bbte.smartguide.model.Regions
import edu.bbte.smartguide.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "GeofenceReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent?.hasError() == true) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        when (
            val geofenceTransition = geofencingEvent?.geofenceTransition
        ) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {

                val notificationBuilder = context?.let {
                    NotificationCompat.Builder(it, "location")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Geofence")
                        .setContentText("You have entered the geofence")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                }

                val notificationManager = context?.let { NotificationManagerCompat.from(it) }
                notificationBuilder?.build()?.let { notificationManager?.notify(2, it) }


                val id = geofencingEvent.triggeringGeofences?.last()?.requestId
                var region: Regions? = null
                var mp: MediaPlayer? = null

                if (id != null) {
                    val call: Call<Regions> = RetrofitInstance.apiService.getRegionById(id.toLong())
                    call.enqueue(object : Callback<Regions> {
                        override fun onResponse(call: Call<Regions>, response: Response<Regions>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    region = it


                                    // play audio

                                    Log.d(TAG, "Entered: ${region?.name} ")

                                }
                            } else {
                                Log.d("API_RESPONSE", "Unsuccessful response: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<Regions>, t: Throwable) {
                            Log.d("Failed Retrieve", "Network Error", t)
                        }
                    })
                }

            }
            else -> {
                // Log the error.
                val errorMessage = "Unknown geofence transition + $geofenceTransition"
                Log.e(TAG, errorMessage)
            }
        }
    }
}