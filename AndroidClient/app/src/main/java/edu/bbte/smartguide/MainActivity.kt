package edu.bbte.smartguide

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import edu.bbte.smartguide.service.tracking.LocationService
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import edu.bbte.smartguide.ui.navigation.Navigation
import edu.bbte.smartguide.ui.theme.SmartGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions += Manifest.permission.POST_NOTIFICATIONS
        }

        ActivityCompat.requestPermissions(
            this,
            permissions,
            0
        )

        setContent {
            SmartGuideTheme {
                Navigation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val geofenceManager = GeofenceManager(this)
        geofenceManager.deregisterGeofence()
        Log.d("LOCATION APP", "Terminated")

        this.startService(Intent(this, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
        })
    }
}
