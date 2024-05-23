package edu.bbte.smartguide.ui.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import edu.bbte.smartguide.ui.viewModel.HomeViewModel

@Composable
fun LocationsList(navHostController: NavHostController, viewModel: HomeViewModel) {
    val locationsData by viewModel.locationsData.collectAsState()

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
                                text = "${locationsData[it].city}\n" +
                                        "${locationsData[it].category}\n",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color(0xFFE2F1E6),
                                    shadow = Shadow(
                                        color = Color.Black, offset = offset, blurRadius = 4f
                                    )
                                )
                            )

                            if (locationsData[it].distance != 0L) {
                                Text(
                                    text = "${locationsData[it].distance} km",
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