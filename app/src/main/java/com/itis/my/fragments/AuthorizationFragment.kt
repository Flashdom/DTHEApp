package com.itis.my.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.itis.my.InfoRepository
import com.itis.my.MainActivity
import com.itis.my.R
import com.itis.my.databinding.FragmentAuthorizationBinding
import com.itis.my.utils.SharedPreferencesManager


class AuthorizationFragment :
    ViewBindingFragment<FragmentAuthorizationBinding>(FragmentAuthorizationBinding::inflate) {

    private val viewModel: AuthorizationViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bnvMenu.visibility = View.GONE
        if (auth.currentUser != null) {
            if (SharedPreferencesManager.isFirstLaunch(requireContext())) {
                viewModel.updateData(requireContext())
            }
            InfoRepository.initUser(auth.currentUser!!)
            navigateToMain()
        } else {
            updateUI()
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }


        binding.btnSave.setOnClickListener {
            if (binding.tietGroup.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), R.string.fill_in_data, Toast.LENGTH_LONG).show()
            } else {
                viewModel.saveUserInfo(binding.tietGroup.text.toString())
                navigateToMain()
            }
        }
    }

    private fun updateUI() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            if (user.group.isBlank()) {
                binding.clAuthContainer.visibility = View.VISIBLE
            } else {
                navigateToMain()
            }
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            InfoRepository.initUser(user!!)
            viewModel.getUserInfo()
            if (SharedPreferencesManager.isFirstLaunch(requireContext())) {
                viewModel.updateData(requireContext())
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.auth_error), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun navigateToMain() {
        SharedPreferencesManager.setLaunched(requireContext())
        (activity as MainActivity).binding.bnvMenu.visibility = View.VISIBLE
        findNavController().navigate(AuthorizationFragmentDirections.actionAuthorizationFragmentToMainFragment())
    }


}