package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import androidx.activity.result.ActivityResult
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthGoogleSign
import kotlinx.coroutines.flow.Flow

class LoginWithGoogle(private val repository : AuthGoogleSign) {

    suspend operator fun invoke(result: ActivityResult) : Flow<Resource<Boolean>> {
       return repository.signWithGoogle(result)
    }
}