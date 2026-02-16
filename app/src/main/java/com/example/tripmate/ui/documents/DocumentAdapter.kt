package com.example.tripmate.ui.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tripmate.data.model.DocumentEntity
import com.example.tripmate.databinding.ItemDocumentBinding

class DocumentAdapter(
    private val onDeleteClick: (DocumentEntity) -> Unit
) : ListAdapter<DocumentEntity, DocumentAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemDocumentBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = getItem(position)

        holder.binding.tvFileName.text = document.fileName

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(document)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DocumentEntity>() {
        override fun areItemsTheSame(old: DocumentEntity, new: DocumentEntity) =
            old.id == new.id

        override fun areContentsTheSame(old: DocumentEntity, new: DocumentEntity) =
            old == new
    }
}