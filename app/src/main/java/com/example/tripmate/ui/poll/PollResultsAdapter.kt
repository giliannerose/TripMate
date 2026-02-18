package com.example.tripmate.ui.poll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmate.data.model.PollEntity
import androidx.recyclerview.widget.ListAdapter
import com.example.tripmate.R
import androidx.recyclerview.widget.DiffUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripmate.ui.poll.PollViewModel

class PollResultsAdapter :
    ListAdapter<PollEntity, PollResultsAdapter.ResultViewHolder>(DiffCallback()) {


    inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuestion: TextView = view.findViewById(R.id.tvQuestion)
        val tvOptions: TextView = view.findViewById(R.id.tvOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poll_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val poll = getItem(position)

        holder.tvQuestion.text = poll.question

        val options = listOfNotNull(
            poll.option1,
            poll.option2,
            poll.option3,
            poll.option4
        )

        holder.tvOptions.text = options.joinToString("\n")
    }

    class DiffCallback : DiffUtil.ItemCallback<PollEntity>() {
        override fun areItemsTheSame(old: PollEntity, new: PollEntity) =
            old.id == new.id

        override fun areContentsTheSame(old: PollEntity, new: PollEntity) =
            old == new
    }
}