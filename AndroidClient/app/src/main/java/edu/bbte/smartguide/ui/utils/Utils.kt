package edu.bbte.smartguide.ui.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


fun checkForPermission(context: Context): Boolean {
     return !(ActivityCompat.checkSelfPermission(
         context,
         Manifest.permission.ACCESS_FINE_LOCATION
     ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
         context,
         Manifest.permission.ACCESS_COARSE_LOCATION
     ) != PackageManager.PERMISSION_GRANTED)
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationFetched: (location: LatLng) -> Unit) {
    var loc: LatLng
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                loc = LatLng(latitude,longitude)
                onLocationFetched(loc)
            }
        }
        .addOnFailureListener { exception: Exception ->
            // Handle failure to get location
            Log.d("MAP-EXCEPTION",exception.message.toString())
        }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation0(context: Context): LatLng {
    var loc: LatLng? = null
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                loc = LatLng(latitude,longitude)
            }
        }
        .addOnFailureListener { exception: Exception ->
            // Handle failure to get location
            Log.d("MAP-EXCEPTION",exception.message.toString())
        }

    return loc!!
}
