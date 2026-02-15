package com.example.tripmate.ui.itinerary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.R
import com.example.tripmate.data.model.ActivityEntity
import android.widget.Button

class ActivityAdapter(
    private val onEditClick: (ActivityEntity) -> Unit,
    private val onDeleteClick: (ActivityEntity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private var activities = emptyList<ActivityEntity>()

    class ActivityViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tvActivityTitle)
        val tvTime: TextView = itemView.findViewById(R.id.tvActivityTime)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditActivity)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteActivity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_itinerary_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val current = activities[position]

        holder.tvTitle.text = current.title
        holder.tvTime.text = "${current.date} • ${current.time}"

        holder.btnEdit.setOnClickListener {
            onEditClick(current)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(current)
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun setActivities(activityList: List<ActivityEntity>) {
        activities = activityList
        notifyDataSetChanged()
    }
}