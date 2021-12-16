package com.vdevtor.schedulemeapp.presentation.feature_login

sealed class AuthStateInfo {
    object Success : AuthStateInfo()
    object Loading : AuthStateInfo()
    object LoggedOut: AuthStateInfo()
    data class AuthError(val message: String) : AuthStateInfo()
}