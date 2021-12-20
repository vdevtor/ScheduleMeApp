package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

data class AuthUseCases(
    val loginAnonymously : LoginAnonymously,
    val registerAccountWithCredentials : RegisterWithEmailPassWord,
    val buildGoogleClient: BuildGoogleClient,
    val loginWithGoogle : LoginWithGoogle
)
