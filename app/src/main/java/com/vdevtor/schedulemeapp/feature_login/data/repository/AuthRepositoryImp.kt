package com.vdevtor.schedulemeapp.feature_login.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vdevtor.common.core.Resource
import com.vdevtor.data.local.entity.AppUserModelDto
import com.vdevtor.domain.repository.UserRepository
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthRepository
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isEmailValid
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isPassWordStrongEnough
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val userRepository: UserRepository
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
        userInfo: AppUserModelDto,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        if (userInfo.email.isEmpty() || password.isBlank()) {
            emit(Resource.Error(context.getString(R.string.blank_field_error)))
            return@flow
        }
        if (userInfo.email.isEmailValid() && userInfo.email.isNotBlank()) {
            if (password.isPassWordStrongEnough() && password.isNotBlank()) {
                val result = auth.createUserWithEmailAndPassword(userInfo.email, password)
                kotlinx.coroutines.delay(2000)
                when (result.isSuccessful) {
                    true -> {
                        if (isUserAuthenticatedInFirebase()) {
                            val user = hashMapOf(
                                "name" to userInfo.name,
                                "email" to userInfo.email,
                                "phone" to userInfo.phone,
                                "account type" to userInfo.accountType
                            )
                            val storageResult =
                                fireStore.collection("users").document(auth.currentUser?.uid ?: "")
                                    .set(user)
                            kotlinx.coroutines.delay(2500)
                            if (storageResult.isSuccessful){
                                userRepository.insertNewUserAtDataBase(userInfo.copy(
                                    userUID = auth.currentUser?.uid
                                ))
                                emit(Resource.Success(true))
                            } else {
                                Log.d("storage", "firebaseSignUpWithCredentials: ${storageResult.exception}")
                                auth.currentUser?.delete()
                                emit(
                                    Resource.Error<Boolean>(
                                        result.exception?.message ?: context.getString(
                                            R.string.email_password_error
                                        )
                                    )
                                )
                            }
                        }
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
            } else emit(Resource.EmailError<Boolean>(context.getString(R.string.invalid_password)))
        } else emit(Resource.PasswordError<Boolean>(context.getString(R.string.invalid_email)))
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

        if (email.isEmailValid() && email.isNotBlank()) {
            if (password.isPassWordStrongEnough() && password.isNotBlank()) {
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
            } else emit(Resource.EmailError<Boolean>(context.getString(R.string.invalid_password)))
        } else emit(Resource.PasswordError<Boolean>(context.getString(R.string.invalid_email)))
    }
}