package com.example.tripmate.ui.poll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.R
import com.example.tripmate.data.model.PollEntity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil

class PollAdapter(
    private val onVoteClick: (PollEntity, String) -> Unit
) : ListAdapter<PollEntity, PollAdapter.PollViewHolder>(DiffCallback()) {

    inner class PollViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuestion: TextView = view.findViewById(R.id.tvQuestion)
        val rgOptions: RadioGroup = view.findViewById(R.id.rgOptions)
        val btnSubmit: Button = view.findViewById(R.id.btnSubmitVote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poll, parent, false)
        return PollViewHolder(view)
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        val poll = getItem(position)

        holder.tvQuestion.text = poll.question

        holder.rgOptions.removeAllViews()

        val options = listOfNotNull(
            poll.option1,
            poll.option2,
            poll.option3,
            poll.option4
        )

        options.forEach { optionText ->
            val radioButton = RadioButton(holder.itemView.context)
            radioButton.text = optionText
            holder.rgOptions.addView(radioButton)
        }

        holder.btnSubmit.setOnClickListener {
            val selectedId = holder.rgOptions.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedOption =
                    holder.itemView.findViewById<RadioButton>(selectedId).text.toString()

                onVoteClick(poll, selectedOption)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PollEntity>() {
        override fun areItemsTheSame(old: PollEntity, new: PollEntity) =
            old.id == new.id

        override fun areContentsTheSame(old: PollEntity, new: PollEntity) =
            old == new
    }
}
