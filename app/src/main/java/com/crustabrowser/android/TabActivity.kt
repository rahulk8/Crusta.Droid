package com.crustabrowser.android

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crustabrowser.android.history.History
import com.crustabrowser.android.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_tab.*

class TabActivity : AppCompatActivity(), DrawerLayout.DrawerListener {
    var tabInfo = TabInfo()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.initDb(this)

        setContentView(R.layout.activity_tab)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        drawer_layout.addDrawerListener(this)
        tab_drawer_button.setOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }
        address_bar.setOnFocusChangeListener { view, _ ->
            run {
                if (!view.hasFocus()) hideKeyboard(view)
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = TabAdapter(tabInfo)
        viewAdapter.onIndexChangeRequested = { index -> onIndexChangeRequested(index) }
        viewAdapter.closeTab = { index -> closeTab(index) }

        recyclerView = recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        drawer_back_button.setOnClickListener {
            tabInfo.currentWebView().goBack()
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        drawer_forward_button.setOnClickListener {
            tabInfo.currentWebView().goForward()
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        drawer_add_tab_button.setOnClickListener {
            addTab()
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        addTab()
    }

    private fun addTab() {
        val tab = Tab(this)
        setupTab(tab)

        changeTab(tab)
    }

    private fun setupTab(tab: Tab) {
        tabInfo.addTab(tab)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun reloadCurrentTab() {
        val webView = tabInfo.currentWebView()
        webView.reload()
    }

    private fun onIndexChangeRequested(index: Int) {
        tabInfo.currentIndex = index

        val tab = tabInfo.tabs[index]
        changeTab(tab)

        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun changeTab(tab: Tab) {
        for (t in tabInfo.tabs) t.isCurrent = false
        tab.isCurrent = true

        frame_layout.removeAllViews()
        frame_layout.addView(tab)

        tab_drawer_button.text = tabInfo.count().toString()
    }

    private fun closeTab(index: Int) {
        tabInfo.tabs.removeAt(index)
        tabInfo.currentIndex--

        if (tabInfo.count() == 0) {
            addTab()
            return
        }

        if (tabInfo.currentIndex < 0) {
            tabInfo.currentIndex = 0
        }

        val tab = tabInfo.tabs[tabInfo.currentIndex]
        changeTab(tab)
    }

    override fun onBackPressed() {
        val webView = tabInfo.currentWebView()
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tab_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reload_button -> {
                reloadCurrentTab()
                return true
            }
            R.id.open_new_tab -> {
                addTab()
                return true
            }
            R.id.show_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is AutoCompleteTextView) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onDrawerStateChanged(newState: Int) {
        viewAdapter.notifyDataSetChanged()
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerOpened(drawerView: View) {
    }

}
