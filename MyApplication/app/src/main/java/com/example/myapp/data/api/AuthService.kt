package com.example.myapp.data.api


import androidx.compose.ui.semantics.Role
import com.example.myapp.data.api.AuthApiService.SignupRequest
import com.example.myapp.data.model.Auth
import com.example.myapp.data.model.User
import com.google.gson.annotations.SerializedName
import dagger.Provides
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun login(@Body request: LoginRequest): Response<AuthTokenResponse>

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<User>

    data class LoginRequest(val email: String, val password: String)
    data class SignupRequest(val username: String, val email: String, val password: String, val role: String)
    data class AuthTokenResponse(  @SerializedName("access_token") val token: String)
}

