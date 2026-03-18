package com.example.tripmate.ui.trip


import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tripmate.databinding.ItemTripBinding
import com.example.tripmate.Trip


class TripAdapter(
    private val onClick: (Trip) -> Unit,
    private val onDelete: (Trip) -> Unit
) : ListAdapter<Trip, TripAdapter.TripViewHolder>(DiffCallback()) {

    inner class TripViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: Trip) {
            binding.tvTripName.text = trip.name
            binding.tvTripDate.text = trip.date
            binding.tvTripDescription.text = trip.description

            binding.root.setOnClickListener {
                onClick(trip)
            }

            binding.btnDeleteTrip.setOnClickListener {
                onDelete(trip)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip) =
            oldItem == newItem
    }
}