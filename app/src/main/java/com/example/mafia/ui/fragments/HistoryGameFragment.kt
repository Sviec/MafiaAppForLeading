package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mafia.App
import com.example.mafia.R
import com.example.mafia.databinding.FragmentHistoryGameBinding
import com.example.mafia.ui.viewmodels.HistoryViewModel


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}