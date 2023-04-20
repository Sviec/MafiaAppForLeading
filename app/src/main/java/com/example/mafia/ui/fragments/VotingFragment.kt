package com.example.mafia.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mafia.databinding.DialogEliminatedPlayerBinding
import com.example.mafia.databinding.DialogTransitionBinding
import com.example.mafia.databinding.FragmentVotingBinding
import com.example.mafia.ui.adapters.VotingAdapter
import com.example.mafia.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class VotingFragment : Fragment() {

    private var _binding: FragmentVotingBinding? = null
    private val binding get() = _binding!!

    private val adapter: VotingAdapter = VotingAdapter()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVotingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playersList.adapter = adapter

        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it.filter { player ->
                player.isExpose
            })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.nextButton.setOnClickListener {
            votingResultsDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun votingResultsDialog() {
        val dialogBinding = DialogTransitionBinding.inflate(layoutInflater)
        dialogBinding.title.text = "Подтвердить итоги голосования?"
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.nextButton.setOnClickListener {
            eliminatedPlayerNotification()
            dialog.dismiss()
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun eliminatedPlayerNotification() {
        val dialogBinding = DialogEliminatedPlayerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val player = viewModel.currentPlayersFlow.value.maxBy { it.votes }
        player.isDead = true
        dialogBinding.playerInfo.text = "${player.number}  ${player.nickname}"
        viewModel.currentPlayersFlow.value.forEach { it.votes = 0 }
        dialogBinding.nextButton.setOnClickListener {
            val act = VotingFragmentDirections.actionVotingFragmentToNightFragment()
            findNavController().navigate(act)
            dialog.dismiss()
        }
        dialog.show()
    }
}