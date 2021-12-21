package com.vdevtor.schedulemeapp.feature_login.domain.repository

import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vdevtor.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthGoogleSign {

    suspend fun buildGoogleClient() : Flow<Resource<GoogleSignInClient>>
    suspend fun signWithGoogle(result: ActivityResult): Flow<Resource<Boolean>>

}