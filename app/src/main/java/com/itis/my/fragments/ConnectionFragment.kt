package com.itis.my.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itis.my.adapters.ConnectionAdapter
import com.itis.my.databinding.FragmentConnectionBinding
import com.itis.my.viewmodels.ConnectionViewModel

class ConnectionFragment : ViewBindingFragment<FragmentConnectionBinding>(FragmentConnectionBinding::inflate) {

    private val viewModel: ConnectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvConnections.apply {
            adapter = ConnectionAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }



    }


}