package com.example.tripmate.ui.trip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.databinding.ItemDashboardTripBinding
import com.example.tripmate.Trip

class DashboardTripAdapter(
    private val onClick: (Trip) -> Unit
) : ListAdapter<Trip, DashboardTripAdapter.TripViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Trip>() {
            override fun areItemsTheSame(old: Trip, new: Trip) =
                old.id == new.id

            override fun areContentsTheSame(old: Trip, new: Trip) =
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