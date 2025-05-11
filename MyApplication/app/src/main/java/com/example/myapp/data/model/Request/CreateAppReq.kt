package com.example.myapp.data.model.Request


data class CreateAppointmentRequest(
    val propertyId: Int,
    val Date: String,
    val startTime: String
)