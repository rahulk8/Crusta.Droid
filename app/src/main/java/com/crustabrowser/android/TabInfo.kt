package com.crustabrowser.android

import kotlinx.android.synthetic.main.view_tab.view.*

class TabInfo(var tabs: MutableList<Tab> = mutableListOf(), var currentIndex: Int = -1) {
    fun count() = tabs.size

    fun addTab(tab: Tab) {
        currentIndex = tabs.size
        tabs.add(tab)
    }

    fun webView(index: Int) = tabs[index].web_view

    fun currentWebView() = tabs[currentIndex].web_view
}