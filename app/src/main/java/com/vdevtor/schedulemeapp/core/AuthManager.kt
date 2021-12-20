package com.vdevtor.schedulemeapp.core

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.vdevtor.schedulemeapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthManager( val auth: FirebaseAuth, private val context: Context) {
    @ExperimentalCoroutinesApi
    fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }

        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun signOutAnonymously(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            auth.currentUser?.apply {
                delete().await()
                if (auth.currentUser != null)
                    emit(Resource.Success(true))
            }
        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_logout_error)))
        }
    }

    suspend fun signOutEmailPassword(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            auth.currentUser?.let {
                auth.signOut()
                kotlinx.coroutines.delay(2000)
                if (auth.currentUser == null) emit(Resource.Success(true))
                else emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_logout_error)))
            }

        } catch (e: Exception) {
            emit(Resource.Error<Boolean>(context.getString(R.string.anonymously_logout_error)))
        }
    }
}