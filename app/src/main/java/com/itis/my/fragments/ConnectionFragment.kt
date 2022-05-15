package com.itis.my.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itis.my.adapters.ConnectionAdapter
import com.itis.my.databinding.FragmentConnectionBinding
import com.itis.my.fragments.adding.AddingNoteFragment
import com.itis.my.model.Connection
import com.itis.my.model.Note
import com.itis.my.viewmodels.ConnectionViewModel

class ConnectionFragment :
    ViewBindingFragment<FragmentConnectionBinding>(FragmentConnectionBinding::inflate) {

    private val viewModel: ConnectionViewModel by viewModels()
    private var connection: Connection? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvConnections.apply {
            adapter = ConnectionAdapter {
                connection = it
                AddingNoteFragment().show(parentFragmentManager, CONNECTION_FRAGMENT_TAG)
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.listenConnections()
        viewModel.connections.observe(viewLifecycleOwner) { connections ->
            (binding.rvConnections.adapter as ConnectionAdapter).updateList(connections)
        }
        setFragmentResultListener(CONNECTION_REQUEST_KEY)
        { _, bundle ->
            val note = bundle.getParcelable<Note>(NotesFragment.NOTE_RESULT_KEY)
            if (note != null && connection != null) {
                viewModel.updateConnection(connection?.apply {
                    feedback = note.text
                }!!)
            }
        }
    }


    companion object {
        private const val CONNECTION_FRAGMENT_TAG = "CFT"
        const val CONNECTION_REQUEST_KEY = "CRK"
        const val CONNECTION_RESULT_KEY = "CRK_REZ"
    }


}