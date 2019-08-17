package com.crustabrowser.android

import android.webkit.WebView

class WebChromeClient : android.webkit.WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)

        val webView = view as com.crustabrowser.android.WebView
        webView.progressBar?.progress = newProgress
    }
}