package edu.bbte.smartguide.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.bbte.smartguide.R
import edu.bbte.smartguide.model.SmallLocation
import edu.bbte.smartguide.retrofit.RetrofitInstance
import edu.bbte.smartguide.ui.viewModel.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Modifier

@Composable
fun LocationsList(navHostController: NavHostController, viewModel: HomeViewModel) {
    val locationsData by viewModel.locationsData.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
//        modifier = Modifier
//            .background(blackV)
//            .fillMaxSize()
    ) {
        items(count = locationsData.size) {
//            LocationsCard(locationsData.value[it], navHostController, viewModel)
            Row(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
//                    .clickable { onItemClick(resId, text) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image1),
                    contentDescription = null,
                    modifier = androidx.compose.ui.Modifier
                        .aspectRatio(16 / 9f)
                        .weight(1f)
                )
                Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
                Column(modifier = androidx.compose.ui.Modifier.weight(1f).padding(10.dp)) {
                    Text(
                        text = locationsData[it].name,
//                        modifier = androidx.compose.ui.Modifier.weight(1f)
                    )
                    Text(
                        text = locationsData[it].city,
//                        modifier = androidx.compose.ui.Modifier.weight(1f)
                    )
                    Text(
                        text = locationsData[it].category,
//                        modifier = androidx.compose.ui.Modifier.weight(1f)
                    )
                    Text(
                        text = "${locationsData[it].distance.toString()} km",
//                        modifier = androidx.compose.ui.Modifier.weight(1f)
                    )
                }
            }
        }

    }
}