package com.example.tripmate.ui.trip

import com.example.tripmate.data.model.TripEntity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tripmate.databinding.ItemTripBinding


class TripAdapter(
    private val onClick: (TripEntity) -> Unit,
    private val onDelete: (TripEntity) -> Unit
) : ListAdapter<TripEntity, TripAdapter.TripViewHolder>(DiffCallback()) {

    inner class TripViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: TripEntity) {
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

    class DiffCallback : DiffUtil.ItemCallback<TripEntity>() {
        override fun areItemsTheSame(oldItem: TripEntity, newItem: TripEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TripEntity, newItem: TripEntity) =
            oldItem == newItem
    }
}