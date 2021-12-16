package com.vdevtor.schedulemeapp.feature_login.di

import com.google.firebase.auth.FirebaseAuth
import com.vdevtor.schedulemeapp.feature_login.data.repository.AuthRepositoryImp
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.AuthUseCases
import com.vdevtor.schedulemeapp.core.AuthManager
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.LoginAnonymously
import org.koin.dsl.module

val scheduleMeModules = module {
    //Repositories
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImp(get(), get()) }

    //UseCases
    factory { AuthUseCases(get()) }
    single { AuthManager(get(), get()) }
    factory { LoginAnonymously(get()) }
}