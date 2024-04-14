package com.example.wakatime.ui.screens.login

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wakatime.ui.WakaViewModel

@Composable
fun WakaLoginScreen(viewModel: WakaViewModel, onLoginSuccess:() -> Unit) {
    LoginWebView { url ->
        viewModel.extractAndSaveAuthDataInDataStore(url)
        onLoginSuccess.invoke()
    }
}

@Composable
fun LoginWebView(onUrlOverride: (url: String) -> Unit) {
    val webViewClient = remember {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                Log.d("webview_tag", url)
                onUrlOverride(url)
                return true
            }
        }
    }
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                this.webViewClient = webViewClient
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl("https://wakatime.com/oauth/authorize?client_id=IYe2gJjSAF0A388BRHIlZP2O&response_type=token&redirect_uri=https://www.youtube.com/")
        }
    )
}