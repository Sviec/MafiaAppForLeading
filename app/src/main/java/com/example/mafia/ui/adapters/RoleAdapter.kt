package com.example.mafia.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
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
    private var singleItemSelectionPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            ModelPurposeNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            number.text = item.number.toString()
            if (singleItemSelectionPosition == position) {
                frame.setBackgroundColor(root.resources.getColor(R.color.red))
            } else {
                frame.setBackgroundColor(Color.TRANSPARENT)
            }
            root.setOnClickListener {
                onItemClick(item)
                setSingleSelection(position)
            }
        }
    }
    private fun setSingleSelection(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        notifyItemChanged(singleItemSelectionPosition)
        singleItemSelectionPosition = adapterPosition
        notifyItemChanged(singleItemSelectionPosition)
    }

}

class RoleViewHolder(val binding: ModelPurposeNumberBinding) : RecyclerView.ViewHolder(binding.root)


class RoleDiffUtil : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.nickname == newItem.nickname
}
