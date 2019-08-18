package com.crustabrowser.android.tabs

import kotlinx.android.synthetic.main.view_tab.view.*

enum class Mode {
    Normal,
    Incognito
}

object TabInfo {
    var normalTabs = mutableListOf<Tab>()
    var incognitoTabs = mutableListOf<Tab>()
    var currentIndex = 0

    var mode = Mode.Normal

    fun count() = if (mode == Mode.Normal) normalTabs.size else incognitoTabs.size

    fun addTab(tab: Tab) {
        if (mode == Mode.Normal) {
            currentIndex = normalTabs.size
            normalTabs.add(tab)
        } else {
            currentIndex = incognitoTabs.size
            incognitoTabs.add(tab)
        }
    }

    fun removeTab(index: Int) {
        if (mode == Mode.Normal) {
            normalTabs.removeAt(index)
            currentIndex--
            if (currentIndex < 0) currentIndex = normalTabs.size - 1
        }
    }

    fun tab(index: Int) = if (mode == Mode.Normal) normalTabs[index] else incognitoTabs[index]

    fun currentTab() = tab(currentIndex)

    fun webView(index: Int) = if (mode == Mode.Normal) normalTabs[index].web_view else incognitoTabs[index].web_view

    fun currentWebView() = webView(currentIndex)
}