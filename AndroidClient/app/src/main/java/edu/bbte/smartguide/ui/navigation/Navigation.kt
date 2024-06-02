package edu.bbte.smartguide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.bbte.smartguide.ui.screen.DetailedLocationScreen
import edu.bbte.smartguide.ui.viewModel.HomeViewModel
import edu.bbte.smartguide.ui.screen.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home"){
            HomeScreen(viewModel = viewModel, navHostController = navController)
        }
        composable("detailedLocation") {
            DetailedLocationScreen(viewModel = viewModel, navHostController = navController)
        }
    }
}