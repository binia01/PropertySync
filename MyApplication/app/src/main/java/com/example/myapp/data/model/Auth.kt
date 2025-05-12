package com.example.myapp.data.model

sealed class Auth{
    object Idle: Auth()
    object Loading : Auth()
    object LoggedOut : Auth()
    data class LoggedIn(val user: User) : Auth()
    data class Error(val message: String) : Auth()
}