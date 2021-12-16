package com.vdevtor.schedulemeapp.feature_login.presentation.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
            authViewModel.logoutAnonymously()
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
                    is AuthStateInfo.LoggedOut -> {
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterToLogin()) }
                    else -> Unit
                }
            }
        }
    }
}