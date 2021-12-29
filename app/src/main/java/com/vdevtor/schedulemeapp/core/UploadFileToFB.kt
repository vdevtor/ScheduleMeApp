package com.vdevtor.schedulemeapp.core

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.vdevtor.common.core.Constants.PROFILE_PIC_PATH
import com.vdevtor.common.core.Constants.PROFILE_PIC_REFERENCE
import com.vdevtor.common.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class UploadFileToFB(
    private val auth: FirebaseAuth, private val storage: FirebaseStorage,
    private val context: Context
) {

    suspend operator fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading<Boolean>())
        val storageRef = storage.reference.child(
            "${auth.currentUser?.uid ?: ""}/$PROFILE_PIC_REFERENCE"+"_${auth.currentUser?.uid}.jpg"
        )

        val file = File(context.filesDir.path + "/$PROFILE_PIC_PATH")
        Log.d("upload", "invoke: ${file.absolutePath}")
        return@flow if (file.exists()) {
            val uri = Uri.fromFile(file)
            val result = storageRef.putFile(uri).await()
            if (result.task.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error<Boolean>("It was not possible to upload your photo"))
            }
        } else {
            emit(Resource.Error<Boolean>("It was not possible to upload your photo"))
        }
    }
}