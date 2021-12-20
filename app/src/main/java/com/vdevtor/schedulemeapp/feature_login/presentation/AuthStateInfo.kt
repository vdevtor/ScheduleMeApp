package com.vdevtor.schedulemeapp.feature_login.presentation

sealed class AuthStateInfo {
    object Success : AuthStateInfo()
    data class SuccessBuildGoogleClient(val data: Any? = null): AuthStateInfo()
    data class SuccessLoginWithGoogle(val data: Any? = null) : AuthStateInfo()
    object Loading : AuthStateInfo()
    object LoggedOut: AuthStateInfo()
    object None: AuthStateInfo()
    data class AuthError(val message: String) : AuthStateInfo()
}