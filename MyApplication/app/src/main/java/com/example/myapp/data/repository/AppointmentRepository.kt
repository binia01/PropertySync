package com.example.myapp.data.repository

import com.example.myapp.data.api.AppointmentService
import com.example.myapp.data.local.AppointmentDao
import com.example.myapp.data.model.AppointmentEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.runCatching

interface AppointmentRepository {
    suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>>
    // Removed other functions for brevity
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
//                    role = determineRole(item),
                    relatedJson = gson.toJson(item)
                )
            }
            appointments
        } else {
            throw Exception("Failed to fetch appointments: ${appointResp.code()} - ${appointResp.message()}")
        }
    }

//    private fun determineRole(appointment: Map<String, Any>): String {
//        return if (appointment.containsKey("buyer")) {
//            "SELLER"
//        } else if (appointment.containsKey("property")) {
//            "BUYER"
//        } else {
//            "UNKNOWN"
//        }
//    }
}




//package com.example.myapp.data.repository

//
//import com.example.myapp.data.api.AppointmentService
//import com.example.myapp.data.local.AppointmentDao
//import com.example.myapp.data.model.AppointmentEntity
//import com.example.myapp.data.model.BuyerBookingsReturn
//import com.example.myapp.data.model.SellerBookingsReturn
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlin.Result
//import kotlin.runCatching
//
//interface AppointmentRepository {
//    suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>>
//    // Removed other functions for brevity, we're focusing on fetching
//}
//
//@Singleton
//class AppointmentRepositoryImpl @Inject constructor(
//    private val appointmentService: AppointmentService,
//    private val gson: Gson
//) : AppointmentRepository {
//
//    override suspend fun refreshAppointments(token: String): Result<List<AppointmentEntity>> = runCatching {
//        println("Token received is $token")
//        val appointResp = appointmentService.getAppointments(token)
//        if (appointResp.isSuccessful && appointResp.body() != null) {
//            val rawJson = gson.toJson(appointResp.body())
//            val appointmentListType = object : TypeToken<List<Map<String, Any>>>() {}.type
//            val rawList = gson.fromJson<List<Map<String, Any>>>(rawJson, appointmentListType)
//
//            val appointments = mutableListOf<AppointmentEntity>()
//            for (item in rawList) {
//                println(item)
//                if (item.containsKey("buyer")) {
//                    // It's a seller appointment
//                    val sellerBookingJson = gson.toJson(item)
//                    val sellerBooking = gson.fromJson(sellerBookingJson, SellerBookingsReturn::class.java)
//                    appointments.add(mapFromSellerNetwork(sellerBooking))
//                } else if (item.containsKey("property")) {
//                    // It's a buyer appointment
//                    val buyerBookingJson = gson.toJson(item)
//                    val buyerBooking = gson.fromJson(buyerBookingJson, BuyerBookingsReturn::class.java)
//                    appointments.add(mapFromBuyerNetwork(buyerBooking))
//                }
//                // You might need more conditions if the structure varies
//            }
//            appointments
//        } else {
//            throw Exception("Failed to fetch appointments: ${appointResp.code()} - ${appointResp.message()}")
//        }
//    }
//
//    private fun mapFromBuyerNetwork(dto: BuyerBookingsReturn): AppointmentEntity {
//        return AppointmentEntity(
//            id = dto.id,
//            createdAt = dto.createdAt,
//            updatedAt = dto.updatedAt,
//            startTime = dto.startTime,
//            date = dto.date,
//            propid = dto.propid,
//            buyerid = dto.buyerid,
//            sellerid = dto.sellerid,
//            status = dto.status,
//            role = "BUYER",
//            relatedJson = gson.toJson(dto.property)
//        )
//    }
//
//    private fun mapFromSellerNetwork(dto: SellerBookingsReturn): AppointmentEntity {
//        return AppointmentEntity(
//            id = dto.id,
//            createdAt = dto.createdAt,
//            updatedAt = dto.updatedAt,
//            startTime = dto.startTime,
//            date = dto.date,
//            propid = dto.propid,
//            buyerid = dto.buyerid,
//            sellerid = dto.sellerid,
//            status = dto.status,
//            role = "SELLER",
//            relatedJson = gson.toJson(dto.buyer)
//        )
//    }
//}
//
//
////package com.example.myapp.data.repository
////
////import com.example.myapp.data.api.AppointmentService
////import com.example.myapp.data.api.AuthApiService
////import com.example.myapp.data.api.UserService
////import com.example.myapp.data.local.AppointmentDao
////import com.example.myapp.data.model.AppointmentEntity
////import com.example.myapp.data.model.BuyerBookingsReturn
////import com.example.myapp.data.model.SellerBookingsReturn
////import com.google.gson.Gson
////import javax.inject.Inject
////
////interface AppointmentRepository {
////    suspend fun getAppointments(): Result<List<AppointmentEntity>>
////    suspend fun saveAppointment(appointment: AppointmentEntity)
////    suspend fun deleteAppointment(id: Int)
////    suspend fun deleteAppointments()
////    suspend fun refreshAppointments(token: String): Result<Any>
////
////}
////
////class AppointmentRepositoryImpl @Inject constructor(
////    private val dao: AppointmentDao,
////    private val appointmentService: AppointmentService,
////    private val gson: Gson
////) : AppointmentRepository {
////    override suspend fun getAppointments() = Result.success(dao.getAllAppointments())
////    override suspend fun saveAppointment(appointment: AppointmentEntity) = dao.insertAppointment(appointment)
////    override suspend fun deleteAppointment(id: Int) = dao.deleteById(id)
////    override suspend fun deleteAppointments() { dao.deleteAll() }
////    override suspend fun refreshAppointments(token: String): Result<Any>{
////        return try {
////            println("TOken recieved is $token")
////            val appointResp = appointmentService.getAppointments(token)
////            if (appointResp.isSuccessful){
////                println("Hello baba ${appointResp.body()}")
////                return Result.success("We Gottem")
////            }else{
////                println("Something Broke ${appointResp}")
////                 Result.failure(Exception("Bloop"))
////            }
////        } catch (e: Exception) {
////            println(e)
////            return Result.failure(Exception("We did not gottem $e"))
////        }
////    }
////    private fun mapFromBuyerNetwork(dto: BuyerBookingsReturn): AppointmentEntity {
////        return AppointmentEntity(
////            id = dto.id,
////            createdAt = dto.createdAt,
////            updatedAt = dto.updatedAt,
////            startTime = dto.startTime,
////            date = dto.date,
////            propid = dto.propid,
////            buyerid = dto.buyerid,
////            sellerid = dto.sellerid,
////            status = dto.status,
////            role = "BUYER",
////            relatedJson = gson.toJson(dto.property)
////        )
////    }
////
////    private fun mapFromSellerNetwork(dto: SellerBookingsReturn): AppointmentEntity {
////        return AppointmentEntity(
////            id = dto.id,
////            createdAt = dto.createdAt,
////            updatedAt = dto.updatedAt,
////            startTime = dto.startTime,
////            date = dto.date,
////            propid = dto.propid,
////            buyerid = dto.buyerid,
////            sellerid = dto.sellerid,
////            status = dto.status,
////            role = "SELLER",
////            relatedJson = gson.toJson(dto.buyer)
////        )
////    }
////}
