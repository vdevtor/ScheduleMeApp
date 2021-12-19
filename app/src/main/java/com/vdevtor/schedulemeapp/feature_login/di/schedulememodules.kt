package com.vdevtor.schedulemeapp.feature_login.di

import com.google.firebase.auth.FirebaseAuth
import com.vdevtor.schedulemeapp.feature_login.data.repository.AuthRepositoryImp
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.core.AuthManager
import com.vdevtor.schedulemeapp.feature_login.data.repository.AuthGoogleSignImp
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthGoogleSign
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.*
import org.koin.dsl.module

val scheduleMeModules = module {
    //Repositories
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImp(get(), get()) }
    single<AuthGoogleSign> { AuthGoogleSignImp(get(), get()) }

    //UseCases
    factory { AuthUseCases(get(),get(),get(),get()) }
    single { AuthManager(get(), get()) }
    factory { LoginAnonymously(get()) }
    factory { LoginWithEmailPassWord(get()) }
    factory { LoginWithGoogle(get()) }
    factory { BuildGoogleClient(get()) }



}