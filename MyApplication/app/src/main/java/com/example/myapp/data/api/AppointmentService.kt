package com.example.myapp.data.api

import com.example.myapp.data.model.BuyerBookingsReturn
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AppointmentService {
    @GET("appointment")
    suspend fun getAppointments(@Header("Authorization") token: String): Response<Any>

}