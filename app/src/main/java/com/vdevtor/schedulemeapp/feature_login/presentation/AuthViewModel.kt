package com.vdevtor.schedulemeapp.feature_login.presentation

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdevtor.schedulemeapp.core.AuthManager
import com.vdevtor.schedulemeapp.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.AuthUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases, private val authManager: AuthManager) :
    ViewModel() {

    private val _state = MutableStateFlow<AuthStateInfo>(AuthStateInfo.None)
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
                    else -> Unit
                }
            }.launchIn(this)
        }
    }

    fun loginWithEmailPassword(email: String,password: String){
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            authUseCases.loginWithEmail(email, password).onEach { resource ->
                when(resource){
                    is Resource.Loading ->{
                        _state.value = AuthStateInfo.Loading
                    }
                    is Resource.Success ->{
                        _state.value = AuthStateInfo.Success
                    }
                    is Resource.EmailError ->{
                        _eventFlow.emit(
                            UiEvent.EmailError(resource.message ?: "Verify your email")
                        )
                    }
                    is Resource.PasswordError ->{
                        _eventFlow.emit(
                            UiEvent.PasswordError(resource.message ?: "Verify your password")
                        )
                    }
                    is Resource.Error ->{
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = resource.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun registerWithCredentials(email: String, password: String) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            authUseCases.registerAccountWithCredentials(email, password).onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = AuthStateInfo.Loading
                    }
                    is Resource.PasswordError -> {
                        _eventFlow.emit(
                            UiEvent.PasswordError(resource.message ?: "Verify your password")
                        )
                    }

                    is Resource.EmailError -> {
                        _eventFlow.emit(
                            UiEvent.EmailError(resource.message ?: "Verify your email")
                        )
                    }
                    is Resource.Error -> {
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

    fun loginWithGoogle(result: ActivityResult) {
        viewModelScope.launch {
            authUseCases.loginWithGoogle(result).onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = AuthStateInfo.SuccessLoginWithGoogle()
                    }
                    is Resource.Error -> {
                       // _state.value = AuthStateInfo.AuthError(resource.message ?: "Unknown Error")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = resource.message ?: "Unknown Error"
                            )
                        )
                    }
                    else -> Unit
                }
            }.launchIn(this)
        }
    }

    fun buildGoogleClient() {
        viewModelScope.launch {
            authUseCases.buildGoogleClient().onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = AuthStateInfo.SuccessBuildGoogleClient(resource.data)
                    }
                    else -> Unit
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

    fun logoutEmailPassword(){
        viewModelScope.launch {
            authManager.signOutEmailPassword().onEach {

            }.launchIn(this)
        }
    }

    @ExperimentalCoroutinesApi
    fun onAuthStateChange() {
        viewModelScope.launch {
            authManager.getFirebaseAuthState().mapLatest {
                if (it == null && _state.value != AuthStateInfo.None) {
                    _state.value = AuthStateInfo.LoggedOut
                }
            }.launchIn(this)
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class PasswordError(val message: String) : UiEvent()
        data class EmailError(val message: String) : UiEvent()
    }
}