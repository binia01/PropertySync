package com.example.myapp.data.model;



// make sure to add a token when we call stuff actually.

data class UserReturn (
        val id: Int?,
        val createdAt: String?,
        val updatedAt: String?,
        val email: String?,
        val name: String?,
        val hash: String?,
        val role: String?,
        val properties: List<ReturnProperty>?,
        val bookedAppointments: List<BuyerBookingsReturn>?,
        val sellingAppointments: List<SellerBookingsReturn>?

)

data class User(
        val id: Int,
        val email: String,
        val firstname: String,
        val lastname: String,
        val role: String,
        val token: String
)