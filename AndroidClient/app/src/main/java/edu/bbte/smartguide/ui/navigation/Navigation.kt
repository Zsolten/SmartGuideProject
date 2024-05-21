package edu.bbte.smartguide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.bbte.smartguide.ui.viewModel.HomeViewModel
import edu.bbte.smartguide.ui.screen.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash"){
            HomeScreen(viewModel = viewModel, navHostController = navController)
        }


    //        composable("splash"){
//            SplashScreen(navHostController = navController)
//        }
//        composable("home") {
//            HomeScreen(viewModel = viewModel, navHostController = navController)
//        }
//        composable("detail_agent") {
//            DetailAgents(viewModel = viewModel, navHostController = navController)
//        }
//        composable("detail_weapon"){
//            DetailWeapon(viewModel = viewModel, navHostController = navController )
//        }
    }
}