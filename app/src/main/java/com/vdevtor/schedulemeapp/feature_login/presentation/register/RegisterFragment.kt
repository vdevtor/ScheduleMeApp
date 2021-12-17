package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentRegisterBinding
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthStateInfo
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn2.setOnClickListener {
            authViewModel.logoutEmailPassword()
        }
        listenToChanges()
        register()
        uiEventListener()
    }

    private fun listenToChanges() {
        authViewModel.onAuthStateChange()
        lifecycleScope.launch {
            authViewModel.state.collectLatest {
                when (it) {
                    is AuthStateInfo.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "registrado com success",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is AuthStateInfo.LoggedOut -> {
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin())
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun uiEventListener() {
        lifecycleScope.launch {
            authViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AuthViewModel.UiEvent.PasswordError -> {
                        Toast.makeText(requireContext(), "Senha invalida", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is AuthViewModel.UiEvent.EmailError -> {
                        Toast.makeText(requireContext(), "email invalido", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is AuthViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun register() {
        binding.register.setOnClickListener {
            val passWord = binding.password.text.toString()
            val email = binding.email.text.toString()
            authViewModel.loginWithEmailPassword(email, passWord)
        }
    }

}