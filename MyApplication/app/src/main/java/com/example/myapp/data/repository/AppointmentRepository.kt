package com.example.myapp.data.repository

import com.example.myapp.data.api.AppointmentService
//import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Request.CreateAppointmentRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.runCatching

interface AppointmentRepository {
    suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>>
    suspend fun updateAppointment(token: String, id: Int, date: String, startTime: String): Result<Unit>
    suspend fun updateAppointmentStatus(token: String, id: Int, status: String, bool: Boolean): Result<Unit>
    suspend fun createAppointment(token: String, requestBody: CreateAppointmentRequest): Result<Unit>

    val appointmentUpdatedFlow: kotlinx.coroutines.flow.SharedFlow<Unit>
}


@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentService: AppointmentService,
    private val gson: Gson
) : AppointmentRepository {
    private val _appointmentUpdatedFlow = MutableSharedFlow<Unit>()
    override val appointmentUpdatedFlow = _appointmentUpdatedFlow.asSharedFlow()

    override suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>> = runCatching {
        println("Token received is $token")
        val appointResp = appointmentService.getAppointments(token)
        println(appointResp.body())
        if (appointResp.isSuccessful && appointResp.body() != null) {
            val rawJson = gson.toJson(appointResp.body())
            val appointmentListType = object : TypeToken<List<Map<String, Any>>>() {}.type
            val rawList = gson.fromJson<List<Map<String, Any>>>(rawJson, appointmentListType)

            val appointments = rawList.map { item ->
                AppointmentEntity(
                    id = (item["id"] as? Double)?.toInt() ?: 0,
                    createdAt = item["createdAt"] as? String ?: "",
                    updatedAt = item["updatedAt"] as? String ?: "",
                    startTime = item["startTime"] as? String ?: "",
                    date = item["Date"] as? String ?: "",
                    propid = (item["propertyId"] as? Double)?.toInt() ?: 0,
                    buyerid = (item["buyerId"] as? Double)?.toInt() ?: 0,
                    sellerid = (item["sellerId"] as? Double)?.toInt() ?: 0,
                    status = item["status"] as? String ?: "",
                    relatedJson = gson.toJson(item)
                )
            }
            appointments
        } else {
            throw Exception("Failed to fetch appointments: ${appointResp.code()} - ${appointResp.message()}")
        }
    }

    override suspend fun updateAppointment(token: String, id: Int, date: String, startTime: String): Result<Unit> = runCatching {

        val requestBody = mapOf(
            "Date" to "${date}T00:00:00.000Z",
            "startTime" to "${date}T${startTime}:00Z" // Include the date with the time
        )
        println("$requestBody")
        val response = appointmentService.editAppointment(token, id, requestBody)
        if (!response.isSuccessful) {
            println(response)
            throw Exception("Failed to update appointment: ${response.code()} - ${response.message()}")
        }
        // Assuming a successful response doesn't need a body for this action
    }

    override suspend fun updateAppointmentStatus(
        token: String,
        id: Int,
        status: String,
        bool: Boolean
    ): Result<Unit> = runCatching {

        if (bool){
            val response = appointmentService.updateAppointmentStatus(token, id, mapOf("status" to status))
            if (!response.isSuccessful) {
                throw Exception("Failed to update appointment status: ${response.code()} - ${response.message()}")
            }
        }else{
            val response =  appointmentService.deleteAppointment(token, id)
            if (!response.isSuccessful) {
                println("Failed to Delete appointment status: ${response.code()} - ${response.message()}")
                throw Exception("Failed to Delete appointment status: ${response.code()} - ${response.message()}")
            }
        }
    }

    override suspend fun createAppointment(token: String, requestBody: CreateAppointmentRequest): Result<Unit> = runCatching {
        println("found token is $token")
        val response = appointmentService.createAppointment("Bearer $token", requestBody)
        if (!response.isSuccessful) {
            throw Exception("Failed to create appointment: ${response.code()} - ${response.message()}")
        }
        _appointmentUpdatedFlow.emit(Unit)
    }
}

