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
import com.example.mafia.R
import com.example.mafia.databinding.DialogEliminatedPlayerBinding
import com.example.mafia.databinding.DialogEqualVotesOfPlayersBinding
import com.example.mafia.databinding.DialogTransitionBinding
import com.example.mafia.databinding.FragmentVotingBinding
import com.example.mafia.ui.adapters.VotingAdapter
import com.example.mafia.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
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

        binding.currentPlayersCount.text =
            viewModel.currentPlayersFlow.value.filter { !it.isDead }.size.toString()
        binding.categoryTitle.text = "Голосование"
        binding.playersList.adapter = adapter

        loadPlayersList()

        binding.nextButton.setOnClickListener {
            confirmVotingResultsDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadPlayersList() {
        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it.filter { player ->
                player.isExpose
            })
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun confirmVotingResultsDialog() {
        val dialogBinding = DialogTransitionBinding.inflate(layoutInflater)
        dialogBinding.title.text = resources.getText(R.string.confirm_voting_results)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.nextButton.setOnClickListener {
            if (checkVotes()) {
                viewModel.votingResults()
                if (checkEliminatePlayers())
                    eliminatedPlayerDialog()
                else {
                    viewModel.clearVotes()
                    equalVotesOfPlayersDialog()
                }
            } else {
                Snackbar
                    .make(binding.root, "Не все голоса распределены", Snackbar.LENGTH_SHORT)
                    .show()
            }
            dialog.dismiss()
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun equalVotesOfPlayersDialog() {
        val dialogBinding = DialogEqualVotesOfPlayersBinding.inflate(layoutInflater)
        dialogBinding.title.text = resources.getText(R.string.confirm_voting_results)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var eliminatePlayersInfo = ""
        viewModel.currentPlayersFlow.value.filter { it.isExpose }.forEach {
            eliminatePlayersInfo += "${it.number}   ${it.nickname}\n"
        }
        dialogBinding.leavePlayersButton.setOnClickListener {
            viewModel.currentPlayersFlow.value.filter { it.isExpose }.forEach {
                it.isExpose = false
            }
            eliminatedPlayerDialog()
            dialog.dismiss()
        }
        dialogBinding.kickPlayersButton.setOnClickListener {
            eliminatedPlayerDialog()
            dialog.dismiss()
        }
        dialogBinding.revotingButton.setOnClickListener {
            loadPlayersList()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun eliminatedPlayerDialog() {
        val dialogBinding = DialogEliminatedPlayerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var eliminatePlayersInfo = ""
        viewModel.currentPlayersFlow.value.filter { it.isExpose }.forEach {
            it.isDead = true
            eliminatePlayersInfo += "${it.number}  ${it.nickname}\n"
        }

        if (eliminatePlayersInfo.isBlank()) {
            dialogBinding.playerInfo.text = "В этот раз никто не покинул игру"
        } else {
            dialogBinding.playerInfo.text = eliminatePlayersInfo
        }
        viewModel.clearVotingInfo()
        dialogBinding.nextButton.setOnClickListener {
            val winner = viewModel.checkWin()
            if (winner > 0) {
                val act = VotingFragmentDirections.actionVotingFragmentToEndFragment(winner)
                findNavController().navigate(act)
            } else {
                viewModel.nightNumber++
                val act = VotingFragmentDirections.actionVotingFragmentToNightFragment()
                findNavController().navigate(act)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkVotes(): Boolean {
        viewModel.currentPlayersFlow.value.apply {
            return sumOf { it.votes } == filter { !it.isDead }.size
        }
    }

    private fun checkEliminatePlayers(): Boolean {
        return viewModel.currentPlayersFlow.value.filter { it.isExpose }.size <= 1
    }
}