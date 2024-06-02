package edu.bbte.smartguide.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import edu.bbte.smartguide.R
import edu.bbte.smartguide.ui.viewModel.HomeViewModel


@Composable
fun DetailedLocationScreen(viewModel: HomeViewModel, navHostController: NavHostController) {
    val data = viewModel.selectedLocation
    val context = LocalContext.current

    data?.let { location ->
        Scaffold(
            floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.googleMapsLink))
                            context.startActivity(intent)
                        },
                        contentColor = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Add")
                    }

            },
            floatingActionButtonPosition = FabPosition.End

        ) { padding ->
            LazyColumn(
                modifier = Modifier.background(Color(0xFFE2F1E6)),
            ) {
                item {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp) // Adjust the height as needed
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(data.pictureUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xFFE2F1E6)
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                        }


//            Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = data.name,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            color = Color.Black
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Nyitvatartas: ",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Text(
                            text = data.openHours,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Belepok: ",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Text(
                            text = data.prices,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = data.description,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

        ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading location data...",
                    color = Color.Black
                )
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}