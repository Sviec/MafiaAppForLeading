package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.R
import com.example.mafia.data.database.player.Player
import com.example.mafia.databinding.ModelEndgamePlayerBinding

class HistoryGameAdapter : ListAdapter<Player, HistoryGameViewHolder>(HistoryGameDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryGameViewHolder {
        return HistoryGameViewHolder(
            ModelEndgamePlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryGameViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            nickname.text = item.nickname
            number.text = item.number.toString()
            role.text = item.role
            points.text = "+${item.points}"
            if (item.isDead == "Мертв") {
                root.setBackgroundColor(root.resources.getColor(R.color.transparent_red))
            }
        }
    }
}

class HistoryGameViewHolder(val binding: ModelEndgamePlayerBinding) : RecyclerView.ViewHolder(binding.root)

class HistoryGameDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}