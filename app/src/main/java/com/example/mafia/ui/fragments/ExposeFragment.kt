package com.example.mafia.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mafia.R
import com.example.mafia.databinding.DialogTransitionBinding
import com.example.mafia.databinding.FragmentExposeBinding
import com.example.mafia.ui.adapters.ExposeAdapter
import com.example.mafia.ui.adapters.IntroAdapter
import com.example.mafia.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ExposeFragment : Fragment() {

    private var _binding: FragmentExposeBinding? = null
    private val binding get() = _binding!!

    private val adapter: ExposeAdapter = ExposeAdapter()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playersList.adapter = adapter

        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it.filter { player -> !player.isDead })
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.nextButton.setOnClickListener {
            confirmExposePlayersDialog()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun confirmExposePlayersDialog() {
        val dialogBinding = DialogTransitionBinding.inflate(layoutInflater)
        dialogBinding.title.text = resources.getText(R.string.confirm_exposed_players)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.nextButton.setOnClickListener {
            val act = ExposeFragmentDirections.actionExposeFragmentToVotingFragment()
            findNavController().navigate(act)
            dialog.dismiss()
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}