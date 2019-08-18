package com.crustabrowser.android.tabs

import com.crustabrowser.android.BrowserMode
import com.crustabrowser.android.TabActivity
import kotlinx.android.synthetic.main.view_tab.view.*

object TabInfo {
    var normalTabs = mutableListOf<Tab>()
    var incognitoTabs = mutableListOf<Tab>()
    var currentIndex = 0

    var mode = BrowserMode.Normal

    lateinit var activity: TabActivity

    fun count() = if (mode == BrowserMode.Normal) normalTabs.size else incognitoTabs.size

    fun addTab(tab: Tab) {
        if (mode == BrowserMode.Normal) {
            currentIndex = normalTabs.size
            normalTabs.add(tab)
        } else {
            currentIndex = incognitoTabs.size
            incognitoTabs.add(tab)
        }
    }

    fun removeTab(index: Int) {
        if (mode == BrowserMode.Normal) {
            val tab = normalTabs[index]
            tab.web_view.destroy()

            normalTabs.removeAt(index)
            currentIndex--
            if (currentIndex < 0) currentIndex = normalTabs.size - 1
        }
    }

    fun tab(index: Int) = if (mode == BrowserMode.Normal) normalTabs[index] else incognitoTabs[index]

    fun currentTab() = tab(currentIndex)

    fun webView(index: Int) = if (mode == BrowserMode.Normal) normalTabs[index].web_view else incognitoTabs[index].web_view

    fun currentWebView() = webView(currentIndex)
}