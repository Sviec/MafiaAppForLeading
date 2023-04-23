package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelEndgamePlayerBinding
import com.example.mafia.entity.Player

class EndAdapter(
) : ListAdapter<Player, EndViewHolder>(EndDiffUtil()) {


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
            points.text = "+${item.points}"
        }
    }
}

class EndViewHolder(val binding: ModelEndgamePlayerBinding) : RecyclerView.ViewHolder(binding.root)

class EndDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}