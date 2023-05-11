package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mafia.App
import com.example.mafia.R
import com.example.mafia.databinding.FragmentEndBinding
import com.example.mafia.ui.adapters.EndAdapter
import com.example.mafia.ui.viewmodels.HistoryViewModel
import com.example.mafia.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class EndFragment : Fragment() {
    private var _binding: FragmentEndBinding? = null
    private val binding get() = _binding!!

    private val adapter: EndAdapter = EndAdapter()
    private val viewModel: MainViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val gameDao = (requireActivity().application as App).db.gameDao()
                val playerDao = (requireActivity().application as App).db.playerDao()
                return HistoryViewModel(gameDao, playerDao) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (navArgs<EndFragmentArgs>().value.winner) {
            1 -> binding.title.text = resources.getText(R.string.peaceful_win)
            2 -> binding.title.text = resources.getText(R.string.mafia_win)
            3 -> binding.title.text = resources.getText(R.string.maniac_win)
            4 -> binding.title.text = resources.getText(R.string.draw)
        }

        binding.results.adapter = adapter
        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        historyViewModel.addGame(viewModel.currentPlayersFlow.value)

        binding.nextButton.setOnClickListener {
            val act = EndFragmentDirections.actionEndFragmentToIntroductionFragment()
            findNavController().navigate(act)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}