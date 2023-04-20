package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelExposePlayerBinding
import com.example.mafia.entity.Player

class ExposeAdapter(
) : ListAdapter<Player, ExposeViewHolder>(ExposeDiffUtil()) {


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
                    exposeButton.text = "Выставить игрока"
                } else {
                    item.isExpose = true
                    exposeButton.text = "Отмена"
                }
            }
        }
    }
}

class ExposeViewHolder(val binding: ModelExposePlayerBinding) : RecyclerView.ViewHolder(binding.root)

class ExposeDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}