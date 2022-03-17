package com.itis.my.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itis.my.adapters.NoteAdapter
import com.itis.my.databinding.NotesFragmentBinding
import com.itis.my.fragments.adding.AddingNoteFragment
import com.itis.my.model.Note
import com.itis.my.viewmodels.NotesViewModel

class NotesFragment : ViewBindingFragment<NotesFragmentBinding>(NotesFragmentBinding::inflate) {


    private val viewModel: NotesViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NoteAdapter()
        }
        viewModel.listenNotesFromDb()
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            (binding.rvNotes.adapter as NoteAdapter).updateList(notes)
        }
        binding.fabAddNote.setOnClickListener {
            AddingNoteFragment().show(parentFragmentManager, NOTES_FRAGMENT_TAG)
        }
        setFragmentResultListener(NOTE_REQUEST_KEY) { _, bundle ->
            val note = bundle.getParcelable<Note>(NOTE_RESULT_KEY)
            if (note != null) {
                viewModel.saveNote(note)
            }
        }
    }

    companion object {
        private const val NOTES_FRAGMENT_TAG = "note_tag"
        const val NOTE_REQUEST_KEY = "note_request"
        const val NOTE_RESULT_KEY = "note_result"
    }

}