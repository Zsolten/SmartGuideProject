package edu.bbte.smartguide.ui.screen

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.bbte.smartguide.service.DefaultService
import edu.bbte.smartguide.ui.permission.PermissionBox
import edu.bbte.smartguide.ui.viewModel.HomeViewModel

@Composable
fun AudioGuideScreen() {
    PermissionBox(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION),
        onGranted = {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionBox(
                    permissions = listOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    BackgroundLocationControls()
                }
            } else {
                BackgroundLocationControls()
            }
        },
    )
}

@Composable
fun BackgroundLocationControls() {
    val context = LocalContext.current
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Az ön személyes mobil Idegenvezetője",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row () {
            Button(onClick = {
                context.startService(Intent(context, DefaultService::class.java).apply {
                    action = DefaultService.ACTION_START
                })
                Log.d("AudioGuideScreen", "Start btn pressed")
            }) {
                Text(text = "Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                context.startService(Intent(context, DefaultService::class.java).apply {
                    action = DefaultService.ACTION_STOP
                })
                Log.d("AudioGuideScreen", "Stop btn pressed")
            }) {
                Text(text = "Stop")
            }
        }
    }
}