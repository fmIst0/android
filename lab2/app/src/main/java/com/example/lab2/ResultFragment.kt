package com.example.lab2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab2.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var listener: OnCancelListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCancelListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCancelListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ResultFragmentArgs.fromBundle(requireArguments())
        binding.textResult.text = args.inputText

        val font = when (args.selectedFont) {
            "Sans-serif" -> android.graphics.Typeface.SANS_SERIF
            "Serif" -> android.graphics.Typeface.SERIF
            "Monospace" -> android.graphics.Typeface.MONOSPACE
            else -> android.graphics.Typeface.DEFAULT
        }
        binding.textResult.typeface = font

        binding.btnCancel.setOnClickListener {
            listener?.onCancelClicked()
            findNavController().popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}