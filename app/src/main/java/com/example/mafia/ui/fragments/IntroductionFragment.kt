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
import com.example.mafia.databinding.DialogAddPlayerBinding
import com.example.mafia.databinding.DialogTransitionBinding
import com.example.mafia.databinding.FragmentIntroductionBinding
import com.example.mafia.entity.Player
import com.example.mafia.entity.Roles
import com.example.mafia.ui.adapters.IntroAdapter
import com.example.mafia.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class IntroductionFragment : Fragment() {
    private var _binding: FragmentIntroductionBinding? = null
    private val binding get() = _binding!!

    private val adapter: IntroAdapter = IntroAdapter(Roles.values()) { deletePlayer(it) }
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listPlayers.adapter = adapter

        viewModel.currentPlayersFlow.onEach {
            adapter.submitList(it)
            it.onEach { player ->
                player.points = 0
                player.isDead = false
                player.role = Roles.NoRule.name_ru
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.addButton.setOnClickListener {
            addPlayerDialog()
        }

        binding.back.setOnClickListener {
            val act = IntroductionFragmentDirections.actionIntroductionFragmentToMainFragment()
            findNavController().navigate(act)
        }
        binding.nextButton.setOnClickListener {
            val isError = checkFilling()
            if (isError.isBlank()) {
                confirmPlayersListDialog()
            } else {
                Snackbar.make(binding.root, isError, Snackbar.LENGTH_SHORT)
                    .setTextMaxLines(3)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkFilling(): String {
        val players = viewModel.currentPlayersFlow.value
        val uniqueNumbers = mutableListOf<Int>()
        val uniqueNicknames = mutableListOf<String>()
        var errorText = ""
        players.forEach {
            if (it.role == Roles.NoRule.name_ru) {
                errorText += "Игрок ${it.number} без роли\n"
            }
            if (uniqueNumbers.contains(it.number)) {
                errorText += "Потворяется номер ${it.number}\n"
            } else {
                uniqueNumbers.add(it.number)
            }
            if (uniqueNicknames.contains(it.nickname)) {
                errorText += "Потворяется никнейм ${it.nickname}\n"
            } else {
                uniqueNicknames.add(it.nickname)
            }
        }
        return errorText
    }

    private fun addPlayerDialog() {
        val dialogBinding = DialogAddPlayerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.addButton.setOnClickListener {
            with (dialogBinding) {
                val number = setNumber.text.toString()
                val nickname = setNickname.text.toString()
                if (number.isBlank()) {
                    setNumber.error = "Обязательное поле"
                } else if (nickname.isBlank()) {
                    setNickname.error = "Обязательное поле"
                } else {
                    viewModel.addPlayer(
                        Player(
                            nickname,
                            number.toInt(),
                            "",
                            isExpose = false,
                            isDead = false,
                            votes = 0,
                            points = 0
                        )
                    )
                    dialog.dismiss()
                }
            }
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun confirmPlayersListDialog() {
        val dialogBinding = DialogTransitionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.nextButton.setOnClickListener {
            viewModel.setRoles()
            val act = IntroductionFragmentDirections.actionIntroductionFragmentToExposeFragment()
            findNavController().navigate(act)
            dialog.dismiss()
        }
        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deletePlayer(player: Player) {
        viewModel.deletePlayer(player)
    }

}