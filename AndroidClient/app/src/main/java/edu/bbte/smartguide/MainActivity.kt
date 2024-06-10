package edu.bbte.smartguide

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import edu.bbte.smartguide.service.DefaultService
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import edu.bbte.smartguide.ui.navigation.Navigation
import edu.bbte.smartguide.ui.theme.SmartGuideTheme

class MainActivity : ComponentActivity() {
    private lateinit var permissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions += Manifest.permission.POST_NOTIFICATIONS
        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            permissions += Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        }

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

        this.startService(Intent(this, DefaultService::class.java).apply {
            action = DefaultService.ACTION_STOP
        })
    }
}





fun main() {
    var nullableString: String? = null
    println(nullableString?.length) // Kiír: null

    nullableString = "Hello"
    println(nullableString.length) // Kiír: 5
}
