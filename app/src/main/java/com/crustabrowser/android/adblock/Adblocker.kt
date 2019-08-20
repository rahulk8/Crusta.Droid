package com.crustabrowser.android.adblock

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.webkit.WebResourceResponse
import com.crustabrowser.android.TabActivity
import java.io.ByteArrayInputStream

object Adblocker {
    private var hosts = hashSetOf<String>()

    fun init(context: Context) {
        AsyncTask.execute {
            val hostList = context.assets.open("hosts.txt").bufferedReader().readLines()
            for (host in hostList) {
                hosts.add(host)
            }
        }
    }

    fun contains(uri: Uri?): Boolean {
        return hosts.contains(uri?.host ?: "")
    }

    fun emptyResponse(): WebResourceResponse {
        return WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream("Blocked by Crusta's Ad Blocker".toByteArray()))
    }
}