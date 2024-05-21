package edu.bbte.smartguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.bbte.smartguide.model.SmallLocation
import edu.bbte.smartguide.retrofit.RetrofitInstance
import edu.bbte.smartguide.ui.navigation.Navigation
import edu.bbte.smartguide.ui.theme.SmartGuideTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGuideTheme {
//                Text(text = "MAINACTIVITY", Modifier.fillMaxWidth().padding(16.dp))
                Navigation()

//                Surface(
//                    Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//                    NavHost(
//                        navController = navController,
//                        startDestination = "list"
//                    ) {
//                        composable("list") {
//                            ListScreen(
//                                onItemClick = {resId, text ->
//                                    navController.navigate("detail/$resId/$text")
//                                }
//                            )
//                        }
//                        composable(
//                            route = "detail/{resId}/{text}",
//                            arguments = listOf(
//                                navArgument("resId") { type = NavType.IntType },
//                                navArgument("text") { type = NavType.StringType }
//                            )
//
//                        ) {
//                            val resId = it.arguments?.getInt("resId") ?: 0
//                            val text = it.arguments?.getString("text") ?: ""
//                            DetailedScreen(resId = resId, text = text)
//                        }
//                    }
//                }
//            }

            }
        }

    }
}
