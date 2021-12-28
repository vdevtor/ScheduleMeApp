package com.vdevtor.common.domain.repopsitory

import com.vdevtor.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun uploadProfilePhotoToFireBase() : kotlinx.coroutines.flow.Flow<Resource<Int>>

    suspend fun savePhotoIntoDB() : Flow<Resource<Boolean>>
}