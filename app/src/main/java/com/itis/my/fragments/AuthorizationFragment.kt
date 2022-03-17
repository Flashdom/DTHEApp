package com.itis.my.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.my.MainActivity
import com.itis.my.R
import com.itis.my.databinding.AuthorizationFragmentBinding
import com.itis.my.model.User
import java.time.Instant

class AuthorizationFragment :
    ViewBindingFragment<AuthorizationFragmentBinding>(AuthorizationFragmentBinding::inflate) {

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bnvMenu.visibility = View.GONE
        if (requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .getBoolean("isAuthorized", false)
        ) {
            (activity as MainActivity).binding.bnvMenu.visibility = View.VISIBLE
            findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMainFragment())
        }
        binding.btnSave.setOnClickListener {
            if (binding.tietFirstName.text.isNullOrBlank() || binding.tietLastName.text.isNullOrBlank() || binding.tietGroup.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fill_in_data),
                    Toast.LENGTH_LONG
                ).show()
            } else {

                viewModel.saveUserInfo(
                    User(
                        binding.tietFirstName.text.toString(),
                        binding.tietLastName.text.toString(),
                        binding.tietGroup.text.toString(),
                        Instant.now().toEpochMilli()
                    )
                )
                (activity as MainActivity).binding.bnvMenu.visibility = View.VISIBLE
                requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
                    .putBoolean("isAuthorized", true).apply()
                findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMainFragment())
            }


        }

    }


}