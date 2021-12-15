package com.vdevtor.schedulemeapp.domain.use_case.auth

import com.vdevtor.schedulemeapp.domain.repository.AuthRepository

class GetAuthState(private val repository: AuthRepository) {
    operator fun invoke() = repository.getFirebaseAuthState()
}