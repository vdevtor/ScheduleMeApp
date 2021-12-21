package com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth

import com.vdevtor.common.core.Resource
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isEmailValid
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isPassWordStrongEnough
import com.vdevtor.schedulemeapp.feature_login.presentation.util.isPasswordEquals
import com.vdevtor.schedulemeapp.feature_login.presentation.util.validateName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterValidationManager {

    fun checkEmailForm(email: String) : Flow<Resource<Boolean>> = flow {
        if (email.isNotBlank()) {
            if (email.isEmailValid()){
                emit(Resource.Success(true))
            }else{
               emit(Resource.Error<Boolean>(email))
            }

        } else emit(Resource.Error<Boolean>("email blank"))

    }

    fun checkNameForm(name: String): Flow<Resource<Boolean>> = flow {
        if (name.validateName()) {
            emit(Resource.Success(true))
        }else emit(Resource.Error<Boolean>("invalid name"))
    }


    fun checkPassWordForm(password: String, confirmPassword: String): Flow<Resource<Boolean>> = flow {
        if (password.isPasswordEquals(confirmPassword)) {
            if (password.isPassWordStrongEnough()){
                emit(Resource.Success(true))

            }else emit(Resource.Error<Boolean>("invalid Password"))

        } else emit(Resource.Error<Boolean>("different passwords"))
    }

}