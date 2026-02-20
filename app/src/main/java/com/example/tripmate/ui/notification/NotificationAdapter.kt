package com.example.tripmate.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.NotificationEntity
import com.example.tripmate.databinding.NotificationItemBinding

class NotificationAdapter(
    private val onActionClick: (NotificationEntity, String) -> Unit
) : ListAdapter<NotificationEntity, NotificationAdapter.NotificationViewHolder>(DiffCallback()) {

    inner class NotificationViewHolder(
        private val binding: NotificationItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationEntity) {

            // Set main texts
            binding.txtTitle.text = notification.type
            binding.txtMessage.text = notification.message

            // Reset visibility
            binding.btnPrimary.visibility = View.GONE
            binding.btnSecondary.visibility = View.GONE

            when (notification.type) {

                "INVITATION" -> {
                    binding.btnPrimary.visibility = View.VISIBLE
                    binding.btnSecondary.visibility = View.VISIBLE

                    binding.btnPrimary.text = "Accept"
                    binding.btnSecondary.text = "Decline"

                    binding.btnPrimary.setOnClickListener {
                        onActionClick(notification, "ACCEPT")
                    }

                    binding.btnSecondary.setOnClickListener {
                        onActionClick(notification, "DECLINE")
                    }
                }

                "POLL" -> {
                    binding.btnPrimary.visibility = View.VISIBLE
                    binding.btnPrimary.text = "View Poll"

                    binding.btnPrimary.setOnClickListener {
                        onActionClick(notification, "VIEW_POLL")
                    }
                }

                "EXPENSE" -> {
                    binding.btnPrimary.visibility = View.VISIBLE
                    binding.btnPrimary.text = "View Expenses"

                    binding.btnPrimary.setOnClickListener {
                        onActionClick(notification, "VIEW_EXPENSE")
                    }
                }

                "INFO" -> {
                    binding.btnPrimary.visibility = View.VISIBLE
                    binding.btnPrimary.text = "Acknowledge"

                    binding.btnPrimary.setOnClickListener {
                        onActionClick(notification, "ACKNOWLEDGE")
                    }
                }
            }

            // Disable buttons if already handled
            if (notification.status != "PENDING") {
                binding.btnPrimary.isEnabled = false
                binding.btnSecondary.isEnabled = false
            } else {
                binding.btnPrimary.isEnabled = true
                binding.btnSecondary.isEnabled = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean = oldItem == newItem
    }
}