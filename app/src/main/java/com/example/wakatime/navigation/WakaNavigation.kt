package com.example.wakatime.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wakatime.ui.WakaViewModel
import com.example.wakatime.ui.screens.login.WakaLoginScreen
import com.example.wakatime.ui.screens.splash.WakaSplashScreen
import com.example.wakatime.ui.screens.summary.WakaSummaryScreen

@Composable
fun WakaNavigation(
    navController: NavHostController = rememberNavController(),
    viewmodel: WakaViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(route = "splash") {
            WakaSplashScreen(viewModel = viewmodel) {token ->
                token?.let {
                    navController.navigate("summary")
                }?: kotlin.run {
                    navController.navigate("login")
                }
            }
        }
        composable(route = "login") {
            WakaLoginScreen(viewModel = viewmodel) {
                navController.navigate(route = "summary")
            }
        }
        composable(route = "summary") {
            WakaSummaryScreen()
        }
    }
}