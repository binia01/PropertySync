package com.example.myapp.data.model;

import androidx.room.Entity
import androidx.room.PrimaryKey

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

@Entity(tableName = "user")
data class User(
        @PrimaryKey val id: Int,
        val email: String,
        val firstname: String,
        val lastname: String,
        val role: String,
        val token: String
)