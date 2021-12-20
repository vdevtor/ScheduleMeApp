package com.vdevtor.schedulemeapp.feature_login.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isEmailValid
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isPassWordStrongEnough
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
    private val context: Context,
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    @ExperimentalCoroutinesApi
    override suspend fun firebaseSignInAnonymously(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val result = auth.signInAnonymously()
            if (result.await().user != null) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_login_error)))
            }

        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_login_error)))
        }
    }

    override suspend fun firebaseSignUpWithCredentials(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        if (email.isBlank() || password.isBlank()) {
            emit(Resource.Error(context.getString(R.string.blank_field_error)))
            return@flow
        }
        if (password.isPassWordStrongEnough()) {
            if (email.isEmailValid()) {
                val result = auth.createUserWithEmailAndPassword(email, password)
                kotlinx.coroutines.delay(2000)
                when (result.isSuccessful) {
                    true -> {
                        if (isUserAuthenticatedInFirebase()) emit(Resource.Success(true))
                    }
                    false -> {
                        emit(
                            Resource.Error<Boolean>(
                                result.exception?.message ?: context.getString(
                                    R.string.email_password_error
                                )
                            )
                        )
                    }
                }
            } else emit(Resource.EmailError<Boolean>(context.getString(R.string.invalid_email)))
        } else emit(Resource.PasswordError<Boolean>(context.getString(R.string.invalid_password)))
    }

    override suspend fun firebaseSignInWithCredentials(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading<Boolean>())

        if (email.isBlank() || password.isBlank()) {
            emit(Resource.Error(context.getString(R.string.blank_field_error)))
            return@flow
        }
        if (password.isPassWordStrongEnough() && password.isNotBlank()) {
            if (email.isEmailValid() && email.isNotBlank()) {
                val result = auth.signInWithEmailAndPassword(email, password)
                kotlinx.coroutines.delay(3100)
                when (result.isSuccessful) {
                    true -> {
                        if (isUserAuthenticatedInFirebase()) emit(Resource.Success(true))
                    }
                    false -> {
                        emit(
                            Resource.Error<Boolean>(
                                result.exception?.message ?: context.getString(
                                    R.string.incomplete_login_error
                                )
                            )
                        )
                    }
                }
            } else emit(Resource.EmailError<Boolean>(context.getString(R.string.invalid_email)))
        } else emit(Resource.PasswordError<Boolean>(context.getString(R.string.invalid_password)))
    }
}