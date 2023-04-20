package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mafia.databinding.FragmentNightBinding
import com.example.mafia.entity.Player
import com.example.mafia.entity.Roles
import com.example.mafia.ui.adapters.RolesAdapter
import com.example.mafia.ui.viewmodels.MainViewModel


class NightFragment : Fragment() {

    private var _binding: FragmentNightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: RolesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RolesAdapter(
            Roles.values().filter { it.priority < 10 },
            viewModel.currentPlayersFlow.value
        ) {
            onNumberClick(it)
        }

        binding.nextButton.setOnClickListener {
            val act = NightFragmentDirections.actionNightFragmentToExposeFragment()
            findNavController().navigate(act)
        }
        binding.playersList.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onNumberClick(player: Player) {

    }
}