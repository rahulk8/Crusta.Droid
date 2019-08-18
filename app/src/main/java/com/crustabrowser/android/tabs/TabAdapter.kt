package com.crustabrowser.android.tabs

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crustabrowser.android.R
import com.crustabrowser.android.TabActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.view_tab.view.*
import kotlinx.android.synthetic.main.view_tab_adapter.view.*

class TabAdapter : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {
    lateinit var activity: TabListActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_tab_adapter, parent,false)
        return TabViewHolder(view)
    }

    override fun getItemCount() = TabInfo.count()

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val webView = TabInfo.webView(position)

        holder.view.favicon.setImageBitmap(webView.favicon)
        holder.view.title.text = webView.title
        holder.view.address.text = webView.url

        val card = holder.view as MaterialCardView
        if (position == TabInfo.currentIndex) {
            card.strokeColor = Color.BLUE
            card.strokeWidth = 2
        } else {
            card.strokeWidth = 0
        }

        holder.view.setOnClickListener {
            val index = holder.adapterPosition
            TabInfo.currentIndex = index
            activity.finish()
        }

        holder.view.close_button.setOnClickListener {
            val index = holder.adapterPosition
            TabInfo.removeTab(index)
            notifyItemRemoved(index)
        }
    }

    class TabViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}