package edu.bbte.smartguide.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import edu.bbte.smartguide.service.tracking.LocationService
import edu.bbte.smartguide.ui.geofence.GeofenceManager
import edu.bbte.smartguide.ui.viewModel.HomeViewModel
import java.io.IOException


@SuppressLint("MissingPermission")
@Composable
fun AudioGuideScreen(navHostController: NavHostController, viewModel: HomeViewModel) {
    val context = LocalContext.current
    val geofenceManager = remember { GeofenceManager(context) }
    val alreadExecuted = remember { mutableStateOf(false) }


//    LaunchedEffect(Unit) {
//        if (!alreadExecuted.value) {
//            geofenceManager.addGeofencesFromRegions(viewModel.regionsData.value)
//            Log.d(">>Home", "LaunchedEffect, regionsData: ${viewModel.regionsData.value.size}")
//            if (geofenceManager.geofenceList.isNotEmpty()) {
//                geofenceManager.registerGeofence()
//            }
//            alreadExecuted.value = true
//        }
//    }


    val url = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"
    Button(onClick = {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        try {
            mediaPlayer.setDataSource(context, Uri.parse(url))
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mp ->
                mp.start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }) {
        Text(text = "Play")
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                context.startService(Intent(context, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                })
            }
        }) {
            Text(text = "Start")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            context.startService(Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
//                viewModel.stopLocationUpdates()
            })
        }) {
            Text(text = "Stop")
        }

//        if (location != null) {
//            Text("Location: ${location?.latitude}, ${location?.longitude}")
//        } else {
//            CircularProgressIndicator()
//        }
    }
}