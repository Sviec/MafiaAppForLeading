package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelRoleBinding
import com.example.mafia.entity.PlayerInfo
import com.example.mafia.entity.Roles

class RolesAdapter(
    private val roles: List<Roles>,
    private val playerInfos: List<PlayerInfo>
): RecyclerView.Adapter<RolesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        return RolesViewHolder(
            ModelRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val item = roles[position]
        val adapter = RoleAdapter { player ->
            holder.binding.purposeRole.text = player.role
            item.purpose = player
        }
        adapter.submitList(
            if (item.playerInfos.count { it.isDead } != item.playerInfos.size)
                playerInfos.filter { !it.isDead }
            else emptyList()
        )
        with(holder.binding) {
            numbersList.adapter = adapter
            role.text = item.name_ru
        }
    }

    override fun getItemCount(): Int = roles.size

}

class RolesViewHolder(val binding: ModelRoleBinding): RecyclerView.ViewHolder(binding.root)