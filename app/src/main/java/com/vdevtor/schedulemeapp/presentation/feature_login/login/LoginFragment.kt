package com.vdevtor.schedulemeapp.presentation.feature_login.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentLoginBinding
import com.vdevtor.schedulemeapp.presentation.feature_login.AuthStateInfo
import com.vdevtor.schedulemeapp.presentation.feature_login.AuthViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val authViewModel: AuthViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginAnonymously()
        uiEvents()
    }

    private fun loginAnonymously() {
        binding.btn.setOnClickListener {
            authViewModel.loginAnonymously()
        }
        requireActivity().lifecycleScope.launchWhenStarted {
            authViewModel.state.collect { state ->
                when (state) {
                    is AuthStateInfo.Success -> {
                        findNavController().navigate(
                            LoginFragmentDirections
                                .actionLoginFragmentToRegisterFragment().actionId
                        )
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

    private fun uiEvents(){
        requireActivity().lifecycleScope.launchWhenStarted {
            authViewModel.eventFlow.collectLatest { event ->
                when(event){
                    is AuthViewModel.UiEvent.ShowSnackbar ->{
                        Snackbar.make(binding.root,event.message,Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}