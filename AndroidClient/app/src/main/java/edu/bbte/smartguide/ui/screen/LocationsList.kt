package edu.bbte.smartguide.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import edu.bbte.smartguide.ui.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun LocationsList(navHostController: NavHostController, viewModel: HomeViewModel) {

    // Loading distance information
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationsData by viewModel.locationsData.collectAsState()

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Location", "Permission granted, lekerem a pontokat")
            locationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener {
                if (it != null) {
                    viewModel.updateWithDistance(it.latitude, it.longitude)
                }
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .background(Color(0xFF182524))
            .fillMaxSize()
    ) {
        items(count = locationsData.size) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .padding(8.dp, 1.dp)
                    .fillMaxWidth()
                    .size(140.dp)
                    .clickable {
                        viewModel.selectLocation(locationsData[it].id)
                        navHostController.navigate("detailedLocation")
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(locationsData[it].pictureUrl),//painterResource(id = R.drawable.image1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0xFF166963))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            val offset = Offset(5.0f, 4.0f)
                            Text(
                                text = locationsData[it].name,
                                style = TextStyle(
                                    fontSize = 27.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE2F1E6),
                                    shadow = Shadow(
                                        color = Color.Black, offset = offset, blurRadius = 3f
                                    )
                                )
                            )

                            Text(
                                text = "${locationsData[it].city}\n"
                                        +
                                        "${locationsData[it].category}\n",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color(0xFFE2F1E6),
                                    shadow = Shadow(
                                        color = Color.Black, offset = offset, blurRadius = 4f
                                    )
                                )
                            )

                            if (locationsData[it].distance != 0.0) {
                                Text(
                                    text = "${locationsData[it].distance.toLong()} km",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color(0xFFE2F1E6),
                                        shadow = Shadow(
                                            color = Color.Black, offset = offset, blurRadius = 4f
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}