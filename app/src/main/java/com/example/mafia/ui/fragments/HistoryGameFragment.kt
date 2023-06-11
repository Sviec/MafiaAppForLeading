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
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mafia.App
import com.example.mafia.R
import com.example.mafia.databinding.FragmentHistoryGameBinding
import com.example.mafia.ui.adapters.EndAdapter
import com.example.mafia.ui.adapters.HistoryGameAdapter
import com.example.mafia.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class HistoryGameFragment : Fragment() {

    private var _binding: FragmentHistoryGameBinding? = null
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
    private val adapter = HistoryGameAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameDate = navArgs<HistoryGameFragmentArgs>().value.gameDate

        binding.playersList.adapter = adapter
        viewModel.getPlayers(gameDate).onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.back.setOnClickListener {
            val act = HistoryGameFragmentDirections.actionHistoryGameFragmentToHistoryFragment()
            findNavController().navigate(act)
        }

        binding.deleteGame.setOnClickListener {
            viewModel.deleteGame(gameDate)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}