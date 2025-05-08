package com.example.myapp.data.model

sealed class UserReqState {
    object Idle: UserReqState()
    object Loading : UserReqState()
    data class Error(val message: String) : UserReqState()
    data class Success(val message: String) : UserReqState()
}