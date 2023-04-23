package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.R
import com.example.mafia.databinding.ModelPurposeNumberBinding
import com.example.mafia.entity.Player

class RoleAdapter(
    private val onItemClick: (Player) -> Unit
) : ListAdapter<Player, RoleViewHolder>(RoleDiffUtil()) {
    private val isClicked = mutableListOf<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            ModelPurposeNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            number.text = item.number.toString()
            root.setOnClickListener {
                onItemClick(item)
                if (isClicked.contains(it)) {
                    frame.setBackgroundColor(root.resources.getColor(R.color.dark_grey))
                    isClicked.remove(it)
                } else if (isClicked.isNotEmpty()) {
                    isClicked[0].setBackgroundColor(root.resources.getColor(R.color.dark_grey))
                    isClicked.removeAt(0)
                    frame.setBackgroundColor(root.resources.getColor(R.color.red))
                } else {
                    isClicked.add(it)
                    frame.setBackgroundColor(root.resources.getColor(R.color.red))
                }
            }
        }
    }
}

class RoleViewHolder(val binding: ModelPurposeNumberBinding) : RecyclerView.ViewHolder(binding.root)

class RoleDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}
