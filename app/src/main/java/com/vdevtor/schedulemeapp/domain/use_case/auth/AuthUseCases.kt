package com.vdevtor.schedulemeapp.domain.use_case.auth

data class AuthUseCases(
    val loginAnonymously : LoginAnonymously,
    val logoutAnonymously: LogoutAnonymously,
    val getAuthState: GetAuthState
)
