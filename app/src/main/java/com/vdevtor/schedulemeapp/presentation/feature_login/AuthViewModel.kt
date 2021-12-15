package com.vdevtor.schedulemeapp.presentation.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.domain.use_case.auth.AuthUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases) : ViewModel() {

    private val _state = MutableStateFlow<AuthStateInfo>(AuthStateInfo.Default)
    val state: StateFlow<AuthStateInfo>
    get() = _state


    fun loginAnonymously(){
        viewModelScope.launch {
            authUseCases.loginAnonymously().onEach { resource ->
                when(resource){
                    is Resource.Loading ->{
                        _state.value = AuthStateInfo.Loading
                    }
                    is Resource.Error -> {
                        _state.value = AuthStateInfo.AuthError(resource.message ?: "Unknown Error")
                    }

                    is Resource.Success ->{
                        _state.value = AuthStateInfo.Success
                    }
                }
            }.launchIn(this)
        }
    }

    fun logoutAnonymously(){
        viewModelScope.launch {
            authUseCases.logoutAnonymously().onEach {

            }.launchIn(this)
        }
    }
}