package com.itis.my.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.itis.my.databinding.FragmentHomeBinding
import com.itis.my.viewmodels.HomeViewModel
import java.time.Instant
import java.time.ZoneId

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()

        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            val zdt = Instant.ofEpochMilli(user.date).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.tvFirstName.text = user.firstName
            binding.tvGroup.text = user.group
            binding.tvLastName.text = user.lastName
            binding.tvCreationDate.text =
                "${zdt.dayOfMonth}.${zdt.month.value}.${zdt.year}  ${zdt.hour}:${zdt.minute}:${zdt.second}"
        }
    }

}