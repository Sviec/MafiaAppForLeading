package com.example.mafia.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.databinding.ModelPreviewPlayerBinding
import com.example.mafia.entity.PlayerInfo
import com.example.mafia.entity.Roles

class IntroAdapter(
    private val rolesList: Array<Roles>,
    private val onDeletePlayer: (PlayerInfo) -> Unit
) : ListAdapter<PlayerInfo, IntroViewHolder>(IntroDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        return IntroViewHolder(
            ModelPreviewPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        val item = getItem(position)
        with (holder.binding) {
            roles.adapter = ArrayAdapter<String>(
                this.root.context,
                android.R.layout.simple_spinner_dropdown_item,
                rolesList.map { it.name_ru }
            )
            nickname.setText(item.nickname)
            number.setText(item.number.toString())
            deletePlayer.setOnClickListener {
                onDeletePlayer(item)
            }
            nickname.addTextChangedListener {
                item.nickname = it.toString()
            }
            number.addTextChangedListener {
                item.number = it.toString().toInt()
            }
            roles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    item.role = rolesList[position].name_ru
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

}

class IntroViewHolder(val binding: ModelPreviewPlayerBinding) : RecyclerView.ViewHolder(binding.root)

class IntroDiffUtil : DiffUtil.ItemCallback<PlayerInfo>() {
    override fun areItemsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem.nickname == newItem.nickname

    override fun areContentsTheSame(oldItem: PlayerInfo, newItem: PlayerInfo): Boolean =
        oldItem == newItem
}