package com.example.tripmate.ui.trip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.TripEntity
import com.example.tripmate.databinding.ItemDashboardTripBinding

class DashboardTripAdapter(
    private val onClick: (TripEntity) -> Unit
) : ListAdapter<TripEntity, DashboardTripAdapter.TripViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<TripEntity>() {
            override fun areItemsTheSame(old: TripEntity, new: TripEntity) =
                old.id == new.id

            override fun areContentsTheSame(old: TripEntity, new: TripEntity) =
                old == new
        }
    }

    inner class TripViewHolder(val binding: ItemDashboardTripBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemDashboardTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = getItem(position)

        holder.binding.tvTripName.text = trip.name
        holder.binding.tvTripDate.text = trip.date

        holder.binding.root.setOnClickListener {
            onClick(trip)
        }
    }
}