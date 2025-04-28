package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab2.databinding.FragmentInputBinding

class InputFragment : Fragment() {
    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            val text = binding.editText.text.toString()
            if (text.isNotBlank()) {
                val selectedFontId = binding.radioGroup.checkedRadioButtonId
                val selectedFont = view.findViewById<RadioButton>(selectedFontId)?.text.toString()

                val fullText = "Text: $text | Font: $selectedFont"

                val success = StorageHelper.saveData(requireContext(), fullText)

                if (success) {
                    Toast.makeText(requireContext(), "Дані успішно збережені", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Помилка збереження даних", Toast.LENGTH_SHORT).show()
                }

                val action = InputFragmentDirections.actionInputFragmentToResultFragment(text, selectedFont)
                findNavController().navigate(action)
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.editText.text.clear()
        }

        binding.btnOpen.setOnClickListener {
            val intent = Intent(requireContext(), StorageActivity::class.java)
            startActivity(intent)
        }
    }


    fun clearInput() {
        binding.editText.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}