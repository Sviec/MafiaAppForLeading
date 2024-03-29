package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.R
import com.example.mafia.databinding.ModelExposePlayerBinding
import com.example.mafia.entity.PlayerInfo

class ExposeAdapter(
) : ListAdapter<PlayerInfo, ExposeViewHolder>(ExposeDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExposeViewHolder {
        return ExposeViewHolder(
            ModelExposePlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExposeViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            nickname.text = item.nickname
            number.text = item.number.toString()
            exposeButton.setOnClickListener {
                if (item.isExpose) {
                    item.isExpose = false
                    exposeButton.setBackgroundColor(root.resources.getColor(R.color.red))
                    exposeButton.text = "Выставить"
                } else {
                    item.isExpose = true
                    exposeButton.setBackgroundColor(root.resources.getColor(R.color.dark_grey))
                    exposeButton.text = "Отмена"
                }
            }
        }
    }
}

class ExposeViewHolder(val binding: ModelExposePlayerBinding) : RecyclerView.ViewHolder(binding.root)

class ExposeDiffUtil : DiffUtil.ItemCallback<PlayerInfo>() {
    override fun areItemsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem.nickname == newItem.nickname
}