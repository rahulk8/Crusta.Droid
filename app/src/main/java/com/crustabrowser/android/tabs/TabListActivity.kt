package com.crustabrowser.android.tabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.crustabrowser.android.R
import kotlinx.android.synthetic.main.activity_tab_list.*

class TabListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_list)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tabs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val linearLayoutManager = LinearLayoutManager(this)
        val tabAdapter = TabAdapter()
        tabAdapter.activity = this
        recycler_view.apply {
            adapter = tabAdapter
            layoutManager = linearLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tab_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.add_tab -> {
                TabInfo.activity.addTab()
                finish()
                return true
            }
            R.id.close_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Close all tabs")
                builder.setPositiveButton("Yes") { _, _ ->
                    TabInfo.removeAllTabs()
                    finish()
                }
                builder.setNegativeButton("No") { _, _ -> }
                val dialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
