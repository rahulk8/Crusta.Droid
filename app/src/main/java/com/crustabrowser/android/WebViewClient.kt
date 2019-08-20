package com.crustabrowser.android

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.AsyncTask
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.crustabrowser.android.adblock.Adblocker
import com.crustabrowser.android.history.History
import java.io.ByteArrayInputStream
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL

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
        if (preferences!!.getBoolean("adblock", true) && Adblocker.contains(uri)) {
            return Adblocker.emptyResponse()
        }

        if (preferences!!.getBoolean("proxy", false)) {
            try {
                val url = URL(uri.toString())
                val host = preferences!!.getString("proxy_host", "localhost")
                val port = preferences!!.getInt("proxy_port", 8000)

                val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
                HttpURLConnection.setFollowRedirects(true)
                val connection = url.openConnection(proxy) as HttpURLConnection
                connection.instanceFollowRedirects = true

                val cookies = CookieManager.getInstance().getCookie(uri.toString())
                connection.addRequestProperty("Cookie", cookies)
                connection.addRequestProperty("User-Agent", view?.settings?.userAgentString)

                val type = connection.contentType ?: "text/plain"
                val encoding = connection.contentEncoding ?: "utf-8"
                val data = connection.inputStream.bufferedReader().readText()

                return WebResourceResponse(type, encoding, ByteArrayInputStream(data.toByteArray()))
            } catch (e: Exception) {
                return WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream("Proxy Error".toByteArray()))
            }

        }
        return super.shouldInterceptRequest(view, request)
    }
}