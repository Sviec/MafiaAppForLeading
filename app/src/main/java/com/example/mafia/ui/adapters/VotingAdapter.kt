package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelExposePlayerBinding
import com.example.mafia.databinding.ModelVotingPlayerBinding
import com.example.mafia.entity.Player

class VotingAdapter: ListAdapter<Player, VotingViewHolder>(VotingDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingViewHolder {
        return VotingViewHolder(
            ModelVotingPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VotingViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            nickname.text = item.nickname
            number.text = item.number.toString()
            minusButton.setOnClickListener {
                if (votesEditable.text.toString().toInt() != 0) {
                    votesEditable.setText((votesEditable.text.toString().toInt() - 1).toString())
                    item.votes--
                } else {
                    it.isClickable = false
                }
            }
            plusButton.setOnClickListener {
                minusButton.isClickable = true
                votesEditable.setText((votesEditable.text.toString().toInt() + 1).toString())
                item.votes++
            }
        }
    }
}

class VotingViewHolder(val binding: ModelVotingPlayerBinding) : RecyclerView.ViewHolder(binding.root)

class VotingDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}