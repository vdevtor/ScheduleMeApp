package com.vdevtor.schedulemeapp.presentation.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.domain.use_case.auth.AuthUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases) : ViewModel() {

    private val _state = MutableStateFlow<AuthStateInfo>(AuthStateInfo.Default)
    val state: StateFlow<AuthStateInfo>
    get() = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun loginAnonymously(){
        viewModelScope.launch {
            authUseCases.loginAnonymously().onEach { resource ->
                when(resource){
                    is Resource.Loading ->{
                        _state.value = AuthStateInfo.Loading
                    }
                    is Resource.Error -> {
                        _state.value = AuthStateInfo.AuthError(resource.message?: "Unknown Error")
                       _eventFlow.emit(
                           UiEvent.ShowSnackbar(
                               message = resource.message ?: "Unknown Error"
                           )
                       )
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


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}