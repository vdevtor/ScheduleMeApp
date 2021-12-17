package com.vdevtor.schedulemeapp.feature_login.presentation

sealed class AuthStateInfo {
    object Success : AuthStateInfo()
    object Loading : AuthStateInfo()
    object LoggedOut: AuthStateInfo()
    object None: AuthStateInfo()
    data class AuthError(val message: String) : AuthStateInfo()
}