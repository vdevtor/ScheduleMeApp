package com.vdevtor.schedulemeapp.presentation.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdevtor.schedulemeapp.core.AuthManager
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.domain.use_case.auth.AuthUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases, private val authManager: AuthManager) :
    ViewModel() {

    private val _state = MutableStateFlow<AuthStateInfo>(AuthStateInfo.LoggedOut)
    val state: StateFlow<AuthStateInfo>
        get() = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var loginJob: Job? = null


    fun loginAnonymously() {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            authUseCases.loginAnonymously().onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = AuthStateInfo.Loading
                    }
                    is Resource.Error -> {
                        _state.value = AuthStateInfo.AuthError(resource.message ?: "Unknown Error")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = resource.message ?: "Unknown Error"
                            )
                        )
                    }

                    is Resource.Success -> {
                        _state.value = AuthStateInfo.Success
                    }
                }
            }.launchIn(this)
        }
    }

    fun logoutAnonymously() {
        viewModelScope.launch {
            authManager.signOutAnonymously().onEach {

            }.launchIn(this)
        }
    }

    @ExperimentalCoroutinesApi
    fun onAuthStateChange() {
        viewModelScope.launch {
            authManager.getFirebaseAuthState().mapLatest {
                if (it == null) {
                    _state.value = AuthStateInfo.LoggedOut
                }
            }.launchIn(this)
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}