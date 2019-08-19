package com.crustabrowser.android.history

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crustabrowser.android.Database
import com.crustabrowser.android.R
import com.crustabrowser.android.tabs.TabInfo
import kotlinx.android.synthetic.main.view_sites_adapter.view.*
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var items = mutableListOf<History>()
    lateinit var activity: HistoryActivity

    init {
        AsyncTask.execute {
            val historyItems = Database.db?.historyDao()?.getAll()
            if (historyItems != null) for (historyItem in historyItems) {
                val index = items.size
                items.add(historyItem)
                notifyItemInserted(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_sites_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.view.title.text = item.title
        holder.view.address.text = item.address
        holder.view.extra_info.text = Date(item.time).toLocaleString()

        holder.view.setOnClickListener {
            TabInfo.addTab(item.address)
            activity.finish()
        }

        holder.view.remove.setOnClickListener {
            val index = holder.adapterPosition
            AsyncTask.execute { Database.db?.historyDao()?.delete(item) }
            items.removeAt(index)

            notifyItemRemoved(index)
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view)
}