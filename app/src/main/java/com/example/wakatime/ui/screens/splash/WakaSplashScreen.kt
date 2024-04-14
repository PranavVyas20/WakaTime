package com.example.wakatime.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.wakatime.ui.WakaViewModel

@Composable
fun WakaSplashScreen(viewModel: WakaViewModel, onTokenAction: (String?) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getTokenFromDataStore { token ->
            onTokenAction(token)
        }
    }
}