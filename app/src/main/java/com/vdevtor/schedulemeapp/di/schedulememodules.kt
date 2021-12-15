package com.vdevtor.schedulemeapp.di

import com.vdevtor.schedulemeapp.data.repository.AuthRepositoryImp
import com.vdevtor.schedulemeapp.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.domain.use_case.auth.AuthUseCases
import com.vdevtor.schedulemeapp.domain.use_case.auth.GetAuthState
import com.vdevtor.schedulemeapp.domain.use_case.auth.LoginAnonymously
import com.vdevtor.schedulemeapp.domain.use_case.auth.LogoutAnonymously
import org.koin.dsl.module

val scheduleMeModules = module {
    //Repositories
    single<AuthRepository>{ AuthRepositoryImp(get()) }

    //UseCases
    factory{ AuthUseCases(get(),get(),get()) }
    factory { GetAuthState(get()) }
    factory { LoginAnonymously(get()) }
    factory { LogoutAnonymously(get()) }
}