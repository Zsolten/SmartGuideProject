package edu.bbte.smartguide.ui.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import edu.bbte.smartguide.model.Regions

class GeofenceManager(context: Context) {
    private val TAG = "GeofenceManager"
    private val client = LocationServices.getGeofencingClient(context)
    val geofenceList = mutableMapOf<String, Geofence>()

    private val geofencingPendingIntent by lazy {
            PendingIntent.getBroadcast(
                context,
                1001,
                Intent(context, GeofenceBroadcastReceiver::class.java),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
    }

    fun addGeofence(
        key: String,
        latitude: Double,
        longitude: Double,
        radiusInMeters: Float = 10000.0f,
    ) {
        geofenceList[key] = createGeofence(key, latitude, longitude, radiusInMeters)
    }


    fun addGeofencesFromRegions(
        regions: List<Regions>,
    ) {
        regions.forEach { region ->
            geofenceList[region.id.toString()] =
                createGeofence(
                    region.id.toString(),
                    region.latitude,
                    region.longitude,
                    region.radius * 1000
                )
            Log.d(TAG, "geofence added: ${region.id}:${region.name}, ${region.latitude}:${region.longitude}")
        }
    }

    fun removeGeofence(key: String) {
        geofenceList.remove(key)
    }

    @SuppressLint("MissingPermission")
    fun registerGeofence() {
        client.addGeofences(createGeofencingRequest(), geofencingPendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "registerGeofence: SUCCESS, list size: ${geofenceList.size}")
            }.addOnFailureListener { exception ->
                Log.d(TAG, "registerGeofence: Failure\n$exception")
            }
    }

    fun deregisterGeofence() = kotlin.runCatching {
        client.removeGeofences(geofencingPendingIntent)
        geofenceList.clear()
    }

    private fun createGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GEOFENCE_TRANSITION_ENTER)
            addGeofences(geofenceList.values.toList())
        }.build()
    }

    private fun createGeofence(
        key: String,
        latitude: Double,
        longitude: Double,
        radiusInMeters: Float,
    ): Geofence {
        return Geofence.Builder()
            .setRequestId(key)
            .setCircularRegion(latitude, longitude, radiusInMeters)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(GEOFENCE_TRANSITION_ENTER)
            .build()
    }

}