package com.crustabrowser.android

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_tab_adapter.view.*

class TabAdapter(private var tabInfo: TabInfo) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {
    var onIndexChangeRequested: ((index: Int) -> Unit)? = null
    var closeTab: ((index: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_tab_adapter, parent,false)
        return TabViewHolder(view)
    }

    override fun getItemCount() = tabInfo.count()

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val webView = tabInfo.webView(position)
        holder.view.favicon.setImageBitmap(webView.favicon)
        holder.view.title.text = webView.title
        holder.view.address.text = webView.url

        if (position == tabInfo.currentIndex) {
            holder.view.setBackgroundColor(Color.rgb(0xf0, 0xf0, 0xf0))
        } else {
            holder.view.setBackgroundColor(Color.WHITE)
        }

        holder.view.setOnClickListener { onIndexChangeRequested?.invoke(position) }
        holder.view.close_button.setOnClickListener {
            closeTab?.invoke(position)
            notifyDataSetChanged()
        }
    }

    class TabViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}