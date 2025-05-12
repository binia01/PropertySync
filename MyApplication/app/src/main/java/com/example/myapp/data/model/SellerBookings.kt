package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName


data class SellerBookingsReturn(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val startTime: String,
    @SerializedName("Date") val date: String,
    @SerializedName("propertyId") val propid: Int,
    @SerializedName("buyerId") val buyerid: Int,
    @SerializedName("sellerId") val sellerid: Int,
    val status: String,
    val buyer: Object,
)
