package edu.bbte.smartguide.ui.geofence

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import edu.bbte.smartguide.model.Regions
import edu.bbte.smartguide.retrofit.RetrofitInstance
import edu.bbte.smartguide.service.DefaultService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "~GeofenceReceiver"

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

                val id = geofencingEvent.triggeringGeofences?.last()?.requestId
                var region: Regions? = null
                

                if (id != null) {
                    val call: Call<Regions> = RetrofitInstance.apiService.getRegionById(id.toLong())
                    call.enqueue(object : Callback<Regions> {
                        override fun onResponse(call: Call<Regions>, response: Response<Regions>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    region = it

                                  // play audio
                                    context?.startService(Intent(context, DefaultService::class.java).apply {
                                        action = DefaultService.ACTION_MEDIA_STOP
                                    })

                                    context?.startService(Intent(context, DefaultService::class.java).apply {
                                        putExtra("AUDIO_URI", region?.description)
                                        action = DefaultService.ACTION_MEDIA_PLAY
                                    })

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