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




// sellingAppointments: [
//
//    {
//
//      id: 1,
//
//      createdAt: 2025-05-08T15:21:33.657Z,
//2007-12-03T10:15:30+01:00
//      updatedAt: 2025-05-08T15:21:33.657Z,
//
//      startTime: 2025-05-09T15:21:33.656Z,
//
//      Date: 2025-05-08T15:21:33.656Z,
//
//      propertyId: 1,
//
//      buyerId: 3,
//
//      sellerId: 1,
//
//      status: 'PENDING',
//
//      buyer={id=5.0, email=buyer1@example.com, firstname=Charlie, lastname=Buyer, role=BUYER, createdAt=2025-05-08T19:26:13.939Z, updatedAt=2025-05-08T19:26:13.939Z}
//
//    }