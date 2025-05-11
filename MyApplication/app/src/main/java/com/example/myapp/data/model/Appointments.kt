package com.example.myapp.data.model



data class AppointmentEntity(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val startTime: String,
    val date: String,
    val propid: Int,
    val buyerid: Int,
    val sellerid: Int,
    val status: String,
    // val role: String, // "BUYER" or "SELLER"
    val relatedJson: String // JSON string of either `property` or `buyer`
)
