package com.vdevtor.common.domain.repopsitory

import android.graphics.Bitmap
import android.net.Uri
import com.vdevtor.common.core.Resource
import com.vdevtor.common.domain.ProfileMediaModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun uploadPhotoToFireBase(uri: Uri? = null, bitmap : Bitmap? = null) : kotlinx.coroutines.flow.Flow<Resource<Int>>

    suspend fun savePhotoIntoDB(profileMedia: ProfileMediaModel) : Flow<Resource<Boolean>>
}