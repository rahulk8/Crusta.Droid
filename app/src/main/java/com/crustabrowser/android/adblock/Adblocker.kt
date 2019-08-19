package com.crustabrowser.android.adblock

import android.net.Uri
import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

object Adblocker {
    private var uris = hashSetOf<Uri>()

    fun init() {

    }

    fun contains(uri: Uri?): Boolean {
        return false
    }

    fun emptyResponse(): WebResourceResponse {
        return WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream("".toByteArray()))
    }
}