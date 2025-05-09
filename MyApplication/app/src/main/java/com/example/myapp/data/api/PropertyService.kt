package com.example.myapp.data.api

import com.example.myapp.data.model.ReturnProperty
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface PropertyService {
    @GET("property")
    suspend fun getProperties(@Header("Authorization") token:String): Response<List<ReturnProperty>>

    @PATCH("property/{id}")
    suspend fun updateProperty(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body updateProperty: UpdateProperty
    ): Response<EditResponse>

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


