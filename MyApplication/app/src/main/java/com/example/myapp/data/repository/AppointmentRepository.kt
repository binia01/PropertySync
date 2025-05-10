package com.example.myapp.data.repository

import com.example.myapp.data.api.AppointmentService
//import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.model.AppointmentEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.runCatching

interface AppointmentRepository {
    suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>>
}

@Singleton
class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentService: AppointmentService,
    private val gson: Gson
) : AppointmentRepository {

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

}

