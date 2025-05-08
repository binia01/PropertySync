package com.example.myapp.data.repository

import com.example.myapp.data.api.AppointmentService
import com.example.myapp.data.api.AuthApiService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.BuyerBookingsReturn
import com.example.myapp.data.model.SellerBookingsReturn
import com.google.gson.Gson
import javax.inject.Inject

interface AppointmentRepository {
    suspend fun getAppointments(): Result<List<AppointmentEntity>>
    suspend fun saveAppointment(appointment: AppointmentEntity)
    suspend fun deleteAppointment(id: Int)
    suspend fun deleteAppointments()
    suspend fun refreshAppointments(token: String): Result<Any>

}

class AppointmentRepositoryImpl @Inject constructor(
    private val dao: AppointmentDao,
    private val appointmentService: AppointmentService,
    private val gson: Gson
) : AppointmentRepository {
    override suspend fun getAppointments() = Result.success(dao.getAllAppointments())
    override suspend fun saveAppointment(appointment: AppointmentEntity) = dao.insertAppointment(appointment)
    override suspend fun deleteAppointment(id: Int) = dao.deleteById(id)
    override suspend fun deleteAppointments() { dao.deleteAll() }
    override suspend fun refreshAppointments(token: String): Result<Any>{
        return try {
            println("TOken recieved is $token")
            val appointResp = appointmentService.getAppointments(token)
            if (appointResp.isSuccessful){
                println("Hello baba ${appointResp.body()}")
                return Result.success("We Gottem")
            }else{
                println("Something Broke ${appointResp}")
                 Result.failure(Exception("Bloop"))
            }
        } catch (e: Exception) {
            println(e)
            return Result.failure(Exception("We did not gottem $e"))
        }
    }
    private fun mapFromBuyerNetwork(dto: BuyerBookingsReturn): AppointmentEntity {
        return AppointmentEntity(
            id = dto.id,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            startTime = dto.startTime,
            date = dto.date,
            propid = dto.propid,
            buyerid = dto.buyerid,
            sellerid = dto.sellerid,
            status = dto.status,
            role = "BUYER",
            relatedJson = gson.toJson(dto.property)
        )
    }

    private fun mapFromSellerNetwork(dto: SellerBookingsReturn): AppointmentEntity {
        return AppointmentEntity(
            id = dto.id,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            startTime = dto.startTime,
            date = dto.date,
            propid = dto.propid,
            buyerid = dto.buyerid,
            sellerid = dto.sellerid,
            status = dto.status,
            role = "SELLER",
            relatedJson = gson.toJson(dto.buyer)
        )
    }
}
