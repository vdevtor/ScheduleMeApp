package com.vdevtor.schedulemeapp.feature_login.presentation

sealed class AuthStateInfo {
    object Success : AuthStateInfo()
    object Loading : AuthStateInfo()
    object LoggedOut: AuthStateInfo()
    data class AuthError(val message: String) : AuthStateInfo()
}