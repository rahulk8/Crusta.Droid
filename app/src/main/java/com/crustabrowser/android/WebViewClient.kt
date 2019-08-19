package com.crustabrowser.android

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.AsyncTask
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.crustabrowser.android.adblock.Adblocker
import com.crustabrowser.android.history.History

class WebViewClient : android.webkit.WebViewClient() {
    var preferences: SharedPreferences? = null

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

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(view?.context)
        }

        val uri = request?.url
        return if (preferences!!.getBoolean("adblock", true) && Adblocker.contains(uri)) Adblocker.emptyResponse()
               else super.shouldInterceptRequest(view, request)
    }
}