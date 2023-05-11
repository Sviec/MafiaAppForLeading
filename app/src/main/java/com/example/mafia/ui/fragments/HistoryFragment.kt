package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mafia.App
import com.example.mafia.data.database.game.Game
import com.example.mafia.databinding.FragmentHistoryBinding
import com.example.mafia.ui.adapters.HistoryAdapter
import com.example.mafia.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val gameDao = (requireActivity().application as App).db.gameDao()
                val playerDao = (requireActivity().application as App).db.playerDao()
                return HistoryViewModel(gameDao, playerDao) as T
            }
        }
    }
    private val adapter: HistoryAdapter = HistoryAdapter { onGameClick(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lastGamesList.adapter = adapter

        viewModel.allGames.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onGameClick(game: Game) {
        val act = HistoryFragmentDirections.actionHistoryFragmentToHistoryGameFragment(game.date)
        findNavController().navigate(act)
    }
}