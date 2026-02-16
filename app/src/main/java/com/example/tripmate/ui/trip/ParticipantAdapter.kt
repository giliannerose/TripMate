package com.example.tripmate.ui.trip

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.UserEntity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import com.example.tripmate.databinding.ItemParticipantBinding


class ParticipantAdapter(
    private val onDelete: (UserEntity) -> Unit,
    private val onEdit: (UserEntity) -> Unit
) : ListAdapter<UserEntity, ParticipantAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemParticipantBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParticipantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)

        holder.binding.tvName.text = user.name
        holder.binding.tvEmail.text = user.email

        holder.binding.icDelete.setOnClickListener {
            onDelete(user)
        }

        holder.binding.icEdit.setOnClickListener {
            onEdit(user)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(old: UserEntity, new: UserEntity) =
            old.id == new.id

        override fun areContentsTheSame(old: UserEntity, new: UserEntity) =
            old == new
    }
}