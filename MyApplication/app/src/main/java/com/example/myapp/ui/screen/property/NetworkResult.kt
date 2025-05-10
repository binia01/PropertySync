package com.example.myapp.ui.screen.property
/**
 * Wrapper class for network responses to handle success, error, and loading states
 */
sealed class NetworkResult<T> {
    class Success<T>(val data: T) : NetworkResult<T>()
    class Error<T>(val message: String) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}