package com.example.myapp.data.api

import com.example.myapp.data.model.User
import com.example.myapp.data.model.UserReturn
import com.example.myapp.data.repository.UserRepository
import com.google.gson.annotations.SerializedName
import dagger.Provides
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import javax.inject.Singleton



interface UserService {
    @GET("users/profile")
    suspend fun getCurrent(@Header("Authorization") token: String): Response<UserReturn>

    @DELETE("users/profile")
    suspend fun delete(@Header("Authorization") authToken: String): Response<Any>

    @PATCH("users/profile")
    suspend fun update(@Header("Authorization") authToken: String, @Body updateRequest: UpdateRequest): Response<UpdateUserResponse>

    data class UpdateRequest(
        val email: String? = null,
        val firstname: String? = null,
        val lastname: String? = null
    )
    data class UpdateUserResponse(@SerializedName("message") val message: String, @SerializedName("user") val userRet: UserReturn)

}

