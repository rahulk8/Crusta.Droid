package com.crustabrowser.android

import android.graphics.Bitmap
import android.os.AsyncTask
import android.webkit.WebView
import androidx.core.view.isVisible
import com.crustabrowser.android.history.History
import java.io.ByteArrayOutputStream
import java.time.Instant

class WebViewClient : android.webkit.WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        val webView = view as com.crustabrowser.android.WebView
        webView.progressBar?.isVisible = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        val webView = view as com.crustabrowser.android.WebView
        webView.progressBar?.isVisible = false

        val history = History(null, webView.title, webView.url, System.currentTimeMillis())
        AsyncTask.execute {
            Database.db?.historyDao()?.insert(history)
        }
    }
}