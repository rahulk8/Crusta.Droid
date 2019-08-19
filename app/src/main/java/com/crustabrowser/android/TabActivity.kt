package com.crustabrowser.android

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crustabrowser.android.bookmarks.Bookmark
import com.crustabrowser.android.bookmarks.BookmarkActivity
import com.crustabrowser.android.history.HistoryActivity
import com.crustabrowser.android.tabs.Tab
import com.crustabrowser.android.tabs.TabInfo
import com.crustabrowser.android.tabs.TabListActivity
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.view_tab_adapter.*

class TabActivity : AppCompatActivity() {

    var forwardButton: MenuItem? = null
    var countButton: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.initDb(this)
        TabInfo.activity = this

        setContentView(R.layout.activity_tab)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        address_bar.setOnFocusChangeListener { view, b -> run {
            if (!view.hasFocus()) {
                hideKeyboard(view)
            }
        }}

        address_bar.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                view.clearFocus()
                val query = view.text.toString()
                TabInfo.currentWebView().search(query)
                true
            } else {
                false
            }
        }

        addTab()
    }

    override fun onResume() {
        super.onResume()

        refreshTabs()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun reloadCurrentTab() {
        val webView = TabInfo.currentWebView()
        webView.reload()
    }

    fun addTab() {
        val tab = Tab(this)
        TabInfo.addTab(tab)

        refreshTabs()
    }

    private fun refreshTabs() {
        if (TabInfo.currentIndex == -1) {
            addTab()
        } else {
            val tab = TabInfo.currentTab()

            frame_layout.removeAllViews()
            frame_layout.addView(tab)

            countButton?.title = TabInfo.count().toString()

            refreshForwardButton()
        }
    }

    private fun refreshForwardButton() {
        if (TabInfo.currentWebView().canGoForward()) {
            forwardButton?.isEnabled = true
            forwardButton?.icon?.alpha = 255
        } else {
            forwardButton?.isEnabled = false
            forwardButton?.icon?.alpha = 25
        }
    }

    override fun onBackPressed() {
        val webView = TabInfo.currentWebView()
        if (webView.canGoBack()) {
            webView.goBack()
            refreshForwardButton()
        }
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tab_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        forwardButton = menu?.findItem(R.id.forward_button)
        refreshForwardButton()

        countButton = menu?.findItem(R.id.tab_list_button)
        countButton?.title = TabInfo.count().toString()
        countButton?.actionView?.setBackgroundResource(R.drawable.tab_list_button)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.forward_button -> {
                TabInfo.currentWebView().goForward()
                refreshForwardButton()
            }
            R.id.reload_button -> {
                reloadCurrentTab()
                return true
            }
            R.id.tab_list_button -> {
                val intent = Intent(this, TabListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.open_new_tab -> {
                addTab()
                return true
            }
            R.id.add_bookmark -> {
                val webView = TabInfo.currentWebView()
                val bookmark = Bookmark(webView.title, webView.url)
                AsyncTask.execute {
                    Database.db?.bookmarkDao()?.insert(bookmark)
                }
            }
            R.id.show_bookmarks -> {
                val intent = Intent(this, BookmarkActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.show_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings_item -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.about_item -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
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
}
