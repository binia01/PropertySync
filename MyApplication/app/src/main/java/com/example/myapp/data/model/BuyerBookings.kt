package com.example.myapp.data.model

import com.google.gson.annotations.SerializedName


data class BuyerBookingsReturn(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val startTime: String,
    @SerializedName("Date") val date: String,
    @SerializedName("propertyId") val propid: Int,
    @SerializedName("buyerId") val buyerid: Int,
    @SerializedName("sellerId") val sellerid: Int,
    val status: String,
    val property: Object,
)

//  bookedAppointments: [
//    {
//      id: 3,
//      createdAt: 2025-05-08T19:26:13.942Z,
//      updatedAt: 2025-05-08T19:26:13.942Z,
//      startTime: 2025-05-09T19:26:13.941Z,
//      Date: 2025-05-08T19:26:13.941Z,
//      propertyId: 2,
//      buyerId: 5,
//      sellerId: 1,
//      status: 'PENDING',
//      property: [Object]
//    }
//  ],