package com.vdevtor.schedulemeapp.presentation.feature_login.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentLoginBinding
import com.vdevtor.schedulemeapp.presentation.feature_login.AuthStateInfo
import com.vdevtor.schedulemeapp.presentation.feature_login.AuthViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val authViewModel: AuthViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginAnonymously()
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
                        Toast.makeText(requireContext(), "DEU RUIM", Toast.LENGTH_SHORT).show()
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
}