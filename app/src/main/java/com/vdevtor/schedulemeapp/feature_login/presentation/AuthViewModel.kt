package com.vdevtor.schedulemeapp.feature_login.presentation

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdevtor.common.core.Resource
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.AuthUseCases
import com.vdevtor.schedulemeapp.feature_login.domain.use_case.auth.RegisterValidationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCases: AuthUseCases, private  val registerValidationManager: RegisterValidationManager) :
    ViewModel() {

    var successEmail: Boolean = false
    var successName: Boolean = false
    var successPassWord: Boolean = false
    var count = 0

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
                        _state.value = AuthStateInfo.None
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
                        _state.value = AuthStateInfo.None
                        _eventFlow.emit(
                            UiEvent.EmailError(resource.message ?: "Verify your email")
                        )
                    }
                    is Resource.PasswordError ->{
                        _state.value = AuthStateInfo.None
                        _eventFlow.emit(
                            UiEvent.PasswordError(resource.message ?: "Verify your password")
                        )
                    }
                    is Resource.Error ->{
                        _state.value = AuthStateInfo.None
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
                        _state.value = AuthStateInfo.None
                        _eventFlow.emit(
                            UiEvent.PasswordError(resource.message ?: "Verify your password")
                        )
                    }

                    is Resource.EmailError -> {
                        _state.value = AuthStateInfo.None
                        _eventFlow.emit(
                            UiEvent.EmailError(resource.message ?: "Verify your email")
                        )
                    }
                    is Resource.Error -> {
                        _state.value = AuthStateInfo.None
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

    fun validatePassword(password: String,confirmPassword:String) : Boolean{
        viewModelScope.launch {
            registerValidationManager.checkPassWordForm(password,confirmPassword).onEach { resource ->
                when(resource){
                    is Resource.Success->{
                        ++count
                        successPassWord = true
                    }
                    is Resource.Error->{
                        --count
                        successPassWord = false
                        resource.message?.let {
                            if (it.contains("different")){
                                _eventFlow.emit(
                                    UiEvent.PassWordDifferent
                                )
                            }else{
                                _eventFlow.emit(
                                    UiEvent.PassWordWeak
                                )
                            }
                        }
                    }
                    else -> Unit
                }
            }.launchIn(this)
        }
        return successPassWord
    }

    fun validateName(name:String) :Boolean{
        viewModelScope.launch {
            registerValidationManager.checkNameForm(name).onEach{ resource ->
                when(resource){
                    is Resource.Success ->{
                        ++count
                        successName = true
                    }
                    is Resource.Error ->{
                        --count
                        successName= false
                        resource.message?.let {
                           _eventFlow.emit(
                               UiEvent.NameError
                           )
                        }
                    }
                    else -> Unit
                }

            }.launchIn(this)
        }
        return successName
    }

    fun validateEmail(email : String) : Boolean{
        viewModelScope.launch {
            registerValidationManager.checkEmailForm(email).onEach{ resource ->
                when(resource){
                    is Resource.Success ->{
                        ++count
                        successEmail = true
                    }
                    is Resource.Error ->{
                        --count
                        successEmail = false
                        resource.message?.let {
                            if (it.contains("blank")){
                                _eventFlow.emit(
                                    UiEvent.EmailBlank
                                )
                            }else{
                                _eventFlow.emit(
                                    UiEvent.EmailError(
                                        it
                                    )
                                )
                            }
                        }
                    }
                    else -> Unit
                }

            }.launchIn(this)
        }
       return successEmail
    }

    fun clearState(){
        _state.value = AuthStateInfo.None
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class PasswordError(val message: String) : UiEvent()
        data class EmailError(val message: String) : UiEvent()
        object EmailBlank : UiEvent()
        object PassWordDifferent : UiEvent()
        object PassWordWeak: UiEvent()
        object NameError : UiEvent()
    }
}