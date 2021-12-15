package com.vdevtor.schedulemeapp.presentation.feature_login.register

import android.os.Bundle
import android.view.View
import com.vdevtor.schedulemeapp.core.BaseFragment
import com.vdevtor.schedulemeapp.databinding.FragmentRegisterBinding
import com.vdevtor.schedulemeapp.presentation.feature_login.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val authViewModel : AuthViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn2.setOnClickListener {
            authViewModel.logoutAnonymously()
        }
    }
}