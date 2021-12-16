package com.vdevtor.schedulemeapp.feature_login.di

import com.vdevtor.schedulemeapp.feature_login.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { AuthViewModel(get(),get()) }
}