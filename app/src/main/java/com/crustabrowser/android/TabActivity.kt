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
import com.crustabrowser.android.bookmarks.BookmarkActivity
import com.crustabrowser.android.history.HistoryActivity
import com.crustabrowser.android.tabs.Tab
import com.crustabrowser.android.tabs.TabInfo
import com.crustabrowser.android.tabs.TabListActivity
import kotlinx.android.synthetic.main.activity_tab.*

class TabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.initDb(this)

        setContentView(R.layout.activity_tab)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        address_bar.setOnFocusChangeListener { view, _ ->
            run {
                if (!view.hasFocus()) hideKeyboard(view)
            }
        }

        tab_list_button.setOnClickListener {
            val intent = Intent(this, TabListActivity::class.java)
            startActivity(intent)
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

    private fun addTab() {
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
        }
    }

    override fun onBackPressed() {
        val webView = TabInfo.currentWebView()
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
