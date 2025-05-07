package com.example.myapp.data.api

import com.example.myapp.data.model.User
import com.example.myapp.data.model.UserReturn
import com.example.myapp.data.repository.UserRepository
import dagger.Provides
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Singleton



interface UserService {
    @GET("users/profile")
    suspend fun getCurrent(@Header("Authorization") token: String): Response<UserReturn>

    @DELETE("users/profile")
    suspend fun delete(@Header("Authorization") authToken: String): Response<Any>

//    @POST("users/profile")
//    suspend fun update(): Response<Unit>

    data class UpdateRequest(val username: String, val firstname: String, val lastname: String )
//    data class AuthTokenResponse(val token: String)
}

