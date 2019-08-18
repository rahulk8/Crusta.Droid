package com.crustabrowser.android

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar

class WebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedWebView(context, attrs, defStyleAttr) {

    var progressBar: ProgressBar? = null

    init {
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()

        settings.javaScriptEnabled = true

        loadUrl("https://google.com")
    }
}