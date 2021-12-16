package com.vdevtor.schedulemeapp.feature_login.presentation.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentLoginBinding
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthStateInfo
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val authViewModel: AuthViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginAnonymously()
        uiEvents()
    }

    private fun loginAnonymously() {
        binding.btn.setOnClickListener {
            authViewModel.loginAnonymously()
        }
        lifecycleScope.launchWhenStarted {
            authViewModel.state.collectLatest { state ->
                when (state) {
                    is AuthStateInfo.Success -> {
                        findNavController().navigate(LoginFragmentDirections.actionLoginToRegister().actionId)
                        binding.progressBar.visibility = View.GONE
                    }
                    is AuthStateInfo.AuthError -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is AuthStateInfo.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun uiEvents() {
        requireActivity().lifecycleScope.launchWhenStarted {
            authViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AuthViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}