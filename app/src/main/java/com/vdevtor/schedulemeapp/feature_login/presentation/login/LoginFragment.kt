package com.vdevtor.schedulemeapp.feature_login.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentLoginBinding
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthStateInfo
import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import com.vdevtor.schedulemeapp.feature_login.presentation.util.navigateWithAnimationsPopUp
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val authViewModel: AuthViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onButtonsClicks()
        uiEvents()
        listenToResponses()
    }

    private fun onButtonsClicks() {
        binding.registerButton.setOnClickListener {
            findNavController().navigateWithAnimationsPopUp(LoginFragmentDirections.actionLoginToRegister().actionId)
        }

        binding.loginButton.setOnClickListener {
            val email =  binding.email.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.loginWithEmailPassword(email, password)
        }

        binding.googleButton.setOnClickListener {
            authViewModel.buildGoogleClient()
        }

        binding.skipButton.setOnClickListener {
            authViewModel.loginAnonymously()
        }

    }

    private fun listenToResponses() {

        lifecycleScope.launch {
            authViewModel.state.collectLatest { state ->
                when (state) {
                    is AuthStateInfo.Success -> {
                        this.cancel()
                        findNavController().navigateWithAnimationsPopUp(LoginFragmentDirections.actionLoginToRegister().actionId)
                    }
                    is AuthStateInfo.AuthError -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is AuthStateInfo.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AuthStateInfo.SuccessBuildGoogleClient -> {
                        val intent = state.data as GoogleSignInClient
                        resultLauncher.launch(intent.signInIntent)
                    }

                    is AuthStateInfo.SuccessLoginWithGoogle -> {
                        this.cancel()
                        findNavController().navigateWithAnimationsPopUp(LoginFragmentDirections.actionLoginToRegister().actionId)
                    }
                    else -> Unit
                }
            }
        }
    }

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            authViewModel.loginWithGoogle(result)
        }

    private fun uiEvents() {
        lifecycleScope.launchWhenStarted {
            authViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AuthViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    is AuthViewModel.UiEvent.EmailError ->{
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is AuthViewModel.UiEvent.PasswordError ->{
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }

                }
            }
        }
    }
}