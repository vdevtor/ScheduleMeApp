package com.vdevtor.schedulemeapp.feature_login.presentation.register

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
            authViewModel.buildGoogleClient()
        }
        listenToChanges()
    }


    private fun listenToChanges() {
        authViewModel.onAuthStateChange()
        lifecycleScope.launch {
            authViewModel.state.collectLatest {
                when (it) {
                    is AuthStateInfo.Success -> {

                    }

                    is AuthStateInfo.SuccessBuildGoogleClient -> {
                        val intent = it.data as GoogleSignInClient
                        resultLauncher.launch(intent.signInIntent)
                    }

                    is AuthStateInfo.SuccessLoginWithGoogle ->{
                        Snackbar.make(binding.root,"Deu Certo loguei com google",
                        Snackbar.LENGTH_SHORT).show()
                    }

                    is AuthStateInfo.LoggedOut -> {
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin())
                    }
                    is AuthStateInfo.AuthError ->{
                        Snackbar.make(binding.root,it.message,
                            Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }


    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            authViewModel.loginWithGoogle(result)
        }
}