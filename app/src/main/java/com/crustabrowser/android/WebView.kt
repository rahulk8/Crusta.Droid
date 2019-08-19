package com.crustabrowser.android

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

class WebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedWebView(context, attrs, defStyleAttr) {

    lateinit var activity: TabActivity
    var progressBar: ProgressBar? = null

    init {
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()

        initSettings()
        
        setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return@setDownloadListener
            }

            val request = DownloadManager.Request(Uri.parse(url))
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
            val cookies = CookieManager.getInstance().getCookie(url)

            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downlaoding file...")
            request.setTitle(fileName)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
        }

        loadHome()
    }

    fun loadHome() {
        loadUrl("https://google.com")
    }

    fun search(query: String) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        settings.apply {
            javaScriptEnabled = preferences.getBoolean("javascript", true)
            blockNetworkImage = !preferences.getBoolean("load_image", true)
            useWideViewPort = preferences.getBoolean("viewport", true)
            saveFormData = preferences.getBoolean("formdata", true)
            setGeolocationEnabled(preferences.getBoolean("location", true))
        }

        CookieManager.getInstance().setAcceptCookie(preferences.getBoolean("cookies", true))
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, preferences.getBoolean("3rd_party_cookies", false))
    }
}