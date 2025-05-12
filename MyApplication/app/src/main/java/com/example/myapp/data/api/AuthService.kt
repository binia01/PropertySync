package com.example.myapp.data.api



import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun login(@Body request: LoginRequest): Response<AuthTokenResponse>

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthTokenResponse>

    data class LoginRequest(val email: String, val password: String)
    data class SignupRequest(val firstname: String, val email: String, val password: String, val role: String)
    data class AuthTokenResponse(  @SerializedName("access_token") val token: String)
}

