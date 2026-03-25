package com.example.tripmate.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.databinding.ItemDashboardActivityBinding

data class DashboardActivity(
    val title: String,
    val dateTime: String
)

class DashboardActivityAdapter : RecyclerView.Adapter<DashboardActivityAdapter.ViewHolder>() {

    private var list = emptyList<DashboardActivity>()

    class ViewHolder(val binding: ItemDashboardActivityBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDashboardActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvActivityTitle.text = item.title
        holder.binding.tvActivityTime.text = item.dateTime
    }

    fun submitList(newList: List<DashboardActivity>) {
        list = newList
        notifyDataSetChanged()
    }
}