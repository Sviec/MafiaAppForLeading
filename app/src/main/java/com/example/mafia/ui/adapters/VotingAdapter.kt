package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelVotingPlayerBinding
import com.example.mafia.entity.PlayerInfo

class VotingAdapter: ListAdapter<PlayerInfo, VotingViewHolder>(VotingDiffUtil()) {

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
            votesEditable.setText(item.votes.toString())
            minusButton.setOnClickListener {
                if (votesEditable.text.toString().toInt() != 0) {
                    item.votes--
                    votesEditable.setText(item.votes.toString())
                } else {
                    it.isClickable = false
                }
            }
            plusButton.setOnClickListener {
                minusButton.isClickable = true
                item.votes++
                votesEditable.setText(item.votes.toString())
            }
        }
    }
}

class VotingViewHolder(val binding: ModelVotingPlayerBinding) : RecyclerView.ViewHolder(binding.root)

class VotingDiffUtil : DiffUtil.ItemCallback<PlayerInfo>() {
    override fun areItemsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem.nickname == newItem.nickname
}