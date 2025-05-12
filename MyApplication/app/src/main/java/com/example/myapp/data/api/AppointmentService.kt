package com.example.myapp.data.api

import com.example.myapp.data.model.Request.CreateAppointmentRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentService {
    @GET("appointment")
    suspend fun getAppointments(@Header("Authorization") token: String): Response<Any>

    @PATCH("appointment/{id}")
    suspend fun editAppointment(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body requestBody: Map<String, String> // Adjust the body based on your DTO
    ): Response<Any> // Or Response<Void> if the backend doesn't return data

    @DELETE("appointment/{id}")
    suspend fun deleteAppointment(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<Any> // Or Response<Void> if the backend doesn't return data

    @PATCH("appointment/{id}/status")
    suspend fun updateAppointmentStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body requestBody: Map<String, String> // Adjust the body based on your DTO
    ): Response<Any> // Or Response<Void>

    @POST("appointment")
    suspend fun createAppointment(
        @Header("Authorization") token: String,
        @Body requestBody: CreateAppointmentRequest // Use Any for propertyId which is Int
    ): Response<Any>

}