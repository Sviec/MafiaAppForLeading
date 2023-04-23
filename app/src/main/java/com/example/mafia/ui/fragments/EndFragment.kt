package com.example.mafia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.mafia.R
import com.example.mafia.databinding.FragmentEndBinding
import com.example.mafia.databinding.FragmentExposeBinding
import com.example.mafia.ui.adapters.EndAdapter
import com.example.mafia.ui.adapters.ExposeAdapter
import com.example.mafia.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class EndFragment : Fragment() {
    private var _binding: FragmentEndBinding? = null
    private val binding get() = _binding!!

    private val adapter: EndAdapter = EndAdapter()
    private val viewModel: MainViewModel by activityViewModels()

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
            1 -> binding.title.text = "Мирные победили"
            2 -> binding.title.text = "Мафия победила"
            3 -> binding.title.text = "Маньяк победил"
            4 -> binding.title.text = "Ничья"
        }

        binding.results.adapter = adapter
        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}