package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.vdevtor.schedulemeapp.core.UploadFileToFB

data class AuthUseCases(
    val loginAnonymously : LoginAnonymously,
    val registerAccountWithCredentials : RegisterWithEmailPassWord,
    val buildGoogleClient: BuildGoogleClient,
    val loginWithGoogle : LoginWithGoogle,
    val loginWithEmail: LoginWithEmail,
    val uploadFileToFB: UploadFileToFB
)
