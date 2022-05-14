package com.itis.my.fragments.adding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.itis.my.R
import com.itis.my.databinding.FragmentAddingNoteBinding
import com.itis.my.fragments.HomeFragment
import com.itis.my.fragments.NotesFragment
import com.itis.my.model.Note
import java.time.Instant


class AddingNoteFragment : DialogFragment() {

    private var _binding: FragmentAddingNoteBinding? = null
    private val binding: FragmentAddingNoteBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentAddingNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnSave.setOnClickListener {
            if (binding.tietNote.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_note_error),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                setFragmentResult(
                    HomeFragment.HOME_REQUEST_KEY,
                    bundleOf(
                        HomeFragment.HOME_REQUEST_KEY to Note(
                            0,
                            binding.tietNote.text.toString(),
                            Instant.now().toEpochMilli()
                        )
                    )
                )
                setFragmentResult(
                    NotesFragment.NOTE_REQUEST_KEY,
                    bundleOf(
                        NotesFragment.NOTE_RESULT_KEY to Note(
                            0,
                            binding.tietNote.text.toString(),
                            Instant.now().toEpochMilli()
                        )
                    )
                )
                dismiss()
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}