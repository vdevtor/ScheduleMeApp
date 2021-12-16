package com.vdevtor.schedulemeapp.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
    private val context: Context
) : AuthRepository {

    private val auth = Firebase.auth
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


    override suspend fun signOutAnonymously(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            auth.currentUser?.apply {
                delete().await()
                if (auth.currentUser != null)
                    emit(Resource.Success(true))
            }
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_logout_error)))
        }
    }

    @ExperimentalCoroutinesApi
    override fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}