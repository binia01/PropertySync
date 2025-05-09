package com.example.myapp.data.api

import com.example.myapp.data.model.ReturnProperty
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface PropertyService {
    @GET("property")
    suspend fun getProperties(@Header("Authorization") token:String): Response<List<ReturnProperty>>

    @PUT("property/{id}")
    suspend fun updateProperty(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body updateProperty: UpdateProperty
    ): Response<ReturnProperty>

    data class UpdateProperty(
        val title: String?,
        val description: String?,
        val area: String?,
        val beds: String?,
        val baths:String?,
        val location: String?,
        val price: String?,
    )
}


