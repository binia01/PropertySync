package com.example.myapp.ui.viewModel

//package com.example.propSync.ui.viewModel;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Auth
import com.example.myapp.data.repository.AuthRepository;
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel(

){
    //    private val authRepo: AuthRepository = AuthRepositoryImpl()
    private val _authState = MutableStateFlow<Auth>(Auth.Idle)
    val authState: StateFlow<Auth> = _authState

    init {
        checkLoggedIn()
    }

    fun login(username: String, password: String){
        _authState.value = Auth.Loading
        viewModelScope.launch {
            val res = authRepository.login(username = username, password = password)
            res.onSuccess { user ->
                println("HII AGAIN FROM LOGIN AUTHVIEW MODEL THE USER HERE IS:    $user")
                _authState.value = Auth.LoggedIn(user = user)
            }.onFailure { error ->
                _authState.value = Auth.Error("Invalid Credentials")
                println("LOGIN FAILURE in ViewModel: ${error.localizedMessage}")
            }

        }
    }

    fun checkLoggedIn(){
        _authState.value= Auth.Loading
        viewModelScope.launch {
            userRepository.getUser()
                .collect{ u -> _authState.value =  if (u != null) Auth.LoggedIn(u) else Auth.LoggedOut }
        }
    }

    fun singUp(username: String, email: String, password: String, role: String){
        _authState.value = Auth.Loading
        println("hello from authviewmodel signup: we got something $username, $email, $role, $password")
        viewModelScope.launch {
            val res = authRepository.signup(username,email,password,role)
            println(res)
            res.onSuccess { user ->
                _authState.value = Auth.LoggedIn(user = user)
            }.onFailure { error ->
                println("SOMETHING AN ERROR???")
                _authState.value = Auth.Error(error.localizedMessage ?: "SignUp failed")
            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = Auth.LoggedOut
        }
    }
}