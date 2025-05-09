package com.example.myapp.data.api

import com.example.myapp.data.model.Request.AddPropertyRequest
import com.example.myapp.data.model.Response.AddPropertyResponse
import com.example.myapp.data.model.ReturnProperty
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PropertyService {
    @GET("property")
    suspend fun getProperties(@Header("Authorization") token:String): Response<List<ReturnProperty>>

    @POST("property")
    suspend fun addProperty(@Header("Authorization") token:String, @Body property: AddPropertyRequest): Response<AddPropertyResponse>

}


