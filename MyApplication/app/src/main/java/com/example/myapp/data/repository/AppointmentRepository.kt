package com.example.myapp.data.repository

import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.model.AppointmentEntity
import javax.inject.Inject

interface AppointmentRepository {
    suspend fun getAppointments(): Result<List<AppointmentEntity>>
    suspend fun saveAppointment(appointment: AppointmentEntity)
    suspend fun deleteAppointment(id: Int)
    suspend fun deleteAppointments()

}

class AppointmentRepositoryImpl @Inject constructor(
    private val dao: AppointmentDao
) : AppointmentRepository {
    override suspend fun getAppointments() = Result.success(dao.getAllAppointments())
    override suspend fun saveAppointment(appointment: AppointmentEntity) = dao.insertAppointment(appointment)
    override suspend fun deleteAppointment(id: Int) = dao.deleteById(id)
    override suspend fun deleteAppointments() { dao.deleteAll() }
}
