package com.example.wakatime.ui.screens.login

import android.graphics.Bitmap
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
                if(url.contains("access_token")) {
                    onUrlOverride(url)
                    return true
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                this.webViewClient = webViewClient
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl("https://wakatime.com/oauth/authorize?client_id=tlndjGhJcjgjl41WpV5ptRsy&response_type=token&redirect_uri=https://www.youtube.com/&scope=read_summaries")
        }
    )
}