package com.example.tripmate.ui.invite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.UserEntity
import com.example.tripmate.databinding.ItemInviteUserBinding

class InviteMembersAdapter :
    ListAdapter<UserEntity, InviteMembersAdapter.UserViewHolder>(DiffCallback()) {

    private val selectedUsers = mutableSetOf<UserEntity>()

    inner class UserViewHolder(
        private val binding: ItemInviteUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserEntity) {
            binding.tvName.text = user.name
            binding.tvEmail.text = user.email

            binding.checkbox.setOnCheckedChangeListener(null)
            binding.checkbox.isChecked = selectedUsers.contains(user)

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedUsers.add(user)
                } else {
                    selectedUsers.remove(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemInviteUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedUsers(): List<UserEntity> =
        selectedUsers.toList()

    class DiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity) =
            oldItem == newItem
    }
}