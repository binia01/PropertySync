package com.example.myapp.data.model.Request

data class AddPropertyRequest (
    val title: String,
    val price: Int,
    val location: String,
    val beds: Int,
    val baths: Int,
    val area: Int,
    val description: String,
)