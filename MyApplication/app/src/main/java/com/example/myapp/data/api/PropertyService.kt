package com.example.myapp.data.api

import androidx.room.Delete
import com.example.myapp.data.model.Request.AddPropertyRequest
import com.example.myapp.data.model.Response.AddPropertyResponse
import com.example.myapp.data.model.ReturnProperty
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.POST

interface PropertyService {
    @GET("property")
    suspend fun getProperties(@Header("Authorization") token:String): Response<List<ReturnProperty>>

    @POST("property")
    suspend fun addProperty(@Header("Authorization") token:String, @Body property: AddPropertyRequest): Response<AddPropertyResponse>


    @PATCH("property/{id}")
    suspend fun updateProperty(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body updateProperty: UpdateProperty
    ): Response<EditResponse>

    @DELETE("property/{id}")
    suspend fun deleteProperty(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Response<Any>

    @GET("property/{id}")
    suspend fun getPropertyById(@Header("Authorization") token: String, @Path("id") id: Int): Response<ReturnProperty>


    data class UpdateProperty(
        val title: String?,
        val description: String?,
        val area: Int?,
        val beds: Int?,
        val baths: Int?,
        val location: String?,
        val price: Int?,
    )
    data class EditResponse(
        val message: String,
        val property: ReturnProperty
    )
}


