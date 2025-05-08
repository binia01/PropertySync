package com.example.myapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val startTime: String,
    val date: String,
    val propid: Int,
    val buyerid: Int,
    val sellerid: Int,
    val status: String,
    val role: String, // "BUYER" or "SELLER"
    val relatedJson: String // JSON string of either `property` or `buyer`
)
