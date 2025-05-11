package com.example.myapp.data.model


import com.google.gson.annotations.SerializedName

data class Property(
    val id: Int,
    val title: String,
    val location: String,
    val price: Int,
    val beds: Int,
    val baths: Int,
    val area: String,
    val imageUrl: String,
    val description: String,
    val sellerId: Int,
    val status: String
)



data class ReturnProperty(
    @SerializedName("id") val id: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("location") val location: String,
    @SerializedName("status") val status: String,
    @SerializedName("sellerId") val sellerId: Int,
    @SerializedName("beds") val beds: Int?,
    @SerializedName("baths") val bathrooms: Int?,
    @SerializedName("area") val area: Int?,
)


