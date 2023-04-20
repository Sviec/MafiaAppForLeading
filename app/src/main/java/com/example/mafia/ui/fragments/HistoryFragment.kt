package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mafia.databinding.FragmentHistoryBinding
import com.example.mafia.entity.Game
import com.example.mafia.ui.adapters.HistoryAdapter


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val games: List<Game> = listOf(
        Game("23:30","09.04.2023"),
        Game("23:00","09.04.2023"),
        Game("22:30","09.04.2023"),
        Game("22:00","09.04.2023"),
        Game("21:30","05.04.2023"),
        Game("21:00","05.04.2023")
    )

    private val adapter: HistoryAdapter = HistoryAdapter()

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

        adapter.submitList(games)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}