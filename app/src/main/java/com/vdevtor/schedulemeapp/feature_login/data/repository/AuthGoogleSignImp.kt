package com.vdevtor.schedulemeapp.feature_login.data.repository

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.vdevtor.schedulemeapp.R
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.repository.AuthGoogleSign
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthGoogleSignImp(private val auth: FirebaseAuth, private val context: Context) :
    AuthGoogleSign {

    override suspend fun buildGoogleClient(): Flow<Resource<GoogleSignInClient>> = flow {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        emit(Resource.Success(GoogleSignIn.getClient(context, googleSignInOptions)))
    }

    override suspend fun signWithGoogle(result: ActivityResult): Flow<Resource<Boolean>> = flow {

        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    account.idToken?.let { token ->
                        val userlogged = firebaseAuthWithGoogle(token)
                        userlogged?.let {
                            emit(Resource.Success(true))
                        } ?: emit(Resource.Error<Boolean>(context.getString(R.string.error_login_with_google)))
                    }
                } else emit(Resource.Error<Boolean>(context.getString(R.string.error_login_with_google)))

            } catch (e: Exception) {
                emit(Resource.Error<Boolean>(context.getString(R.string.error_login_with_google)))
            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(token: String): FirebaseUser? {

        val credentials = GoogleAuthProvider.getCredential(token, null)
        val result = auth.signInWithCredential(credentials).await()
        return if (result.user != null) result.user else null
    }
}