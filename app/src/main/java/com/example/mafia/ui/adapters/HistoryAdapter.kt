package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelLastGameBinding
import com.example.mafia.entity.Game

class HistoryAdapter(
) : ListAdapter<Game, HistoryViewHolder>(HistoryDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ModelLastGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            dateGame.text = item.date
            timeGame.text = item.time
        }
    }
}

class HistoryViewHolder(val binding: ModelLastGameBinding) : RecyclerView.ViewHolder(binding.root)

class HistoryDiffUtil : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
        oldItem.date == newItem.date
}
