package com.vdevtor.schedulemeapp.di

import com.vdevtor.schedulemeapp.presentation.feature_login.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { AuthViewModel(get(),get()) }
}