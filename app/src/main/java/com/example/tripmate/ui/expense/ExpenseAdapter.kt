package com.example.tripmate.ui.expense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.R
import com.example.tripmate.data.model.ExpenseEntity

class ExpenseAdapter(
    private val onDelete: (ExpenseEntity) -> Unit
) : ListAdapter<ExpenseEntity, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    inner class ExpenseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvPaidBy: TextView = itemView.findViewById(R.id.tvPaidBy)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(expense: ExpenseEntity) {
            tvTitle.text = expense.title
            tvAmount.text = "₱ ${expense.amount}"
            tvPaidBy.text = "${expense.paidBy} paid"

            btnDelete.setOnClickListener {
                onDelete(expense)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<ExpenseEntity>() {
        override fun areItemsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpenseEntity, newItem: ExpenseEntity): Boolean {
            return oldItem == newItem
        }
    }
}
