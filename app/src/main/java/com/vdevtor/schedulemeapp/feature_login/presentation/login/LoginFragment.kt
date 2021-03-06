package com.vdevtor.schedulemeapp.feature_login.presentation.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.vdevtor.common.core.BaseFragment
import com.vdevtor.schedulemeapp.R
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
        checkIfAlreadyLogged()
        onButtonsClicks()
        uiEvents()
        listenToResponses()
    }

    private fun checkIfAlreadyLogged() {
        if (authViewModel.isUserAuthenticatedInFirebase()){
            // TO DO GO TO ANOTHER ACTIVITY
            Toast.makeText(requireContext(), "logado", Toast.LENGTH_SHORT).show()
        }
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
                        //TO DO GO TO ANOTHER ACTIVITY
                        authViewModel.clearState()
                    }
                    is AuthStateInfo.AuthError -> {
                        binding.progressBar.visibility = View.GONE
                        authViewModel.clearState()
                    }
                    is AuthStateInfo.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AuthStateInfo.SuccessBuildGoogleClient -> {
                        val intent = state.data as GoogleSignInClient
                        resultLauncher.launch(intent.signInIntent)
                        authViewModel.clearState()
                    }

                    is AuthStateInfo.SuccessLoginWithGoogle -> {
                        this.cancel()
                        // TO DO GO TO ANOTHER ACTIVITY
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
                        binding.email.error = getString(R.string.invalid_email_edit_text_error)

                    }

                    is AuthViewModel.UiEvent.PasswordError ->{
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        binding.password.isEndIconVisible = false
                        binding.passwordEditText.error = getString(R.string.invalid_password_edit_text_error)
                        observeEditTextPassword()
                    }

                }
            }
        }
    }

    private fun observeEditTextPassword() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.password.isEndIconVisible = true
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}