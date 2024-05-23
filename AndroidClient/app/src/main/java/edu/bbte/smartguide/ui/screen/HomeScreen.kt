package edu.bbte.smartguide.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import edu.bbte.smartguide.R
import edu.bbte.smartguide.ui.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navHostController: NavHostController){

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent
    )

    viewModel.update()

    Column (
        modifier = Modifier.background(Color(0xFF182524))
    ){
        Image(
            painter = painterResource(id = R.drawable.smartguide_logo), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
        )

        TabScreen(modifier = Modifier, navHostController, viewModel)
    }
}

@Composable
fun TabScreen(modifier: Modifier, navHostController: NavHostController, viewModel: HomeViewModel) {
    val tabIndex = viewModel.tabIndex

    val tabs = listOf("Lista", "Audio Guide")

    Column(modifier = modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color(0xFF182524),
            contentColor = Color(0xFFE2F1E6),
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    content = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE2F1E6))
                                .height(5.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }
                )
            }) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selectedContentColor = Color(0xFFE2F1E6),
                    text = {
                        Text(
                            title,
                            style = TextStyle(fontSize = 20.sp, color = Color(0xFFE2F1E6))
                        )
                    },
                    selected = tabIndex == index,
                    onClick = { viewModel.tabIndex(index) }
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp).background(Color(0xFFE2F1E6)))
        when (tabIndex) {
            0 -> LocationsList(navHostController, viewModel)
            1 -> AudioGuideScreen(navHostController, viewModel)
        }
    }
}