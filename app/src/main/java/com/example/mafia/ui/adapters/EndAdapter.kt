package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelEndgamePlayerBinding
import com.example.mafia.entity.PlayerInfo

class EndAdapter(
) : ListAdapter<PlayerInfo, EndViewHolder>(EndDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EndViewHolder {
        return EndViewHolder(
            ModelEndgamePlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: EndViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            nickname.text = item.nickname
            number.text = item.number.toString()
            role.text = item.role
            points.text = "+${item.points}"
        }
    }
}

class EndViewHolder(val binding: ModelEndgamePlayerBinding) : RecyclerView.ViewHolder(binding.root)

class EndDiffUtil : DiffUtil.ItemCallback<PlayerInfo>() {
    override fun areItemsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem.nickname == newItem.nickname
}