package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelRoleBinding
import com.example.mafia.entity.Player
import com.example.mafia.entity.Roles

class RolesAdapter(
    private val roles: List<Roles>,
    private val players: List<Player>,
    private val onItemClick: (Player) -> Unit
): RecyclerView.Adapter<RolesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        return RolesViewHolder(
            ModelRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val item = roles[position]
        val adapter = RoleAdapter { onItemClick(it) }
        adapter.submitList(players.filter { !it.isDead })
        with(holder.binding) {
            numbersList.adapter = adapter
            role.text = item.name_ru
        }
    }

    override fun getItemCount(): Int = roles.size
}

class RolesViewHolder(val binding: ModelRoleBinding): RecyclerView.ViewHolder(binding.root)