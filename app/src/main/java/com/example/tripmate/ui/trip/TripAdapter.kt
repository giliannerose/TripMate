package com.example.tripmate.ui.trip


import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.tripmate.databinding.ItemTripBinding
import com.example.tripmate.Trip
import java.util.Locale
import java.util.Calendar
import java.text.SimpleDateFormat
import android.view.View


class TripAdapter(
    private val onClick: (Trip) -> Unit,
    private val onDelete: (Trip) -> Unit
) : ListAdapter<Trip, TripAdapter.TripViewHolder>(DiffCallback()) {

    inner class TripViewHolder(val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: Trip) {
            binding.tvTripName.text = trip.name
            binding.tvTripDate.text = trip.date

            if (trip.description.isNullOrBlank()) {
                binding.tvTripDescription.visibility = View.GONE
            } else {
                binding.tvTripDescription.visibility = View.VISIBLE
                binding.tvTripDescription.text = trip.description
            }

            binding.tvTripStatus.text = getTripStatus(trip.date)

            binding.root.setOnClickListener {
                onClick(trip)
            }

            binding.btnDeleteTrip.setOnClickListener {
                onDelete(trip)
            }
        }

        private fun getTripStatus(dateRange: String): String {
            return try {
                val parts = dateRange.split("-")
                if (parts.size < 2) return "Planned"

                val formatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                val startDate = formatter.parse(parts[0].trim())
                val endDate = formatter.parse(parts[1].trim())

                val today = Calendar.getInstance().time

                when {
                    startDate == null || endDate == null -> "Planned"
                    today.before(startDate) -> "Upcoming"
                    today.after(endDate) -> "Completed"
                    else -> "Ongoing"
                }
            } catch (e: Exception) {
                "Planned"
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