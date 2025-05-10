package com.example.myapp.data.model.Response

data class PropertyResponse (
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val title: String,
    val description: String,
    val price: Int,
    val location: String,
    val status: String,
    val sellerId: Int
)