package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthGoogleSign
import kotlinx.coroutines.flow.Flow

class BuildGoogleClient(private val repository: AuthGoogleSign) {

    suspend operator fun invoke() : Flow<Resource<GoogleSignInClient>> {
        return repository.buildGoogleClient()
    }
}