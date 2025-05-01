package com.example.myapp.data.api

import com.example.myapp.data.model.ReturnProperty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface PropertyService {
    @GET("property")
    suspend fun getProperties(@Header("Authorization") token:String): Response<List<ReturnProperty>>
}


