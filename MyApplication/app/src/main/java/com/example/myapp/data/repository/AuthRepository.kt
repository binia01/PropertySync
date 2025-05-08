package com.example.myapp.data.repository


import com.example.myapp.data.api.AuthApiService
//import com.example.propSync.data.api.RealAuthAPiService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.BuyerBookingsReturn
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.SellerBookingsReturn
import com.example.myapp.data.model.User
import com.google.gson.Gson
import javax.inject.Inject
import kotlin.toString

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun signup(username: String, email: String, password: String, role: String): Result<User>
    suspend fun logout()
    suspend fun savePropertiesSeller(property: Property)
}


class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository,
    private val userService: UserService,
    private val propertyDao: PropertyDAO // will change to propertyRepo
): AuthRepository
{
    val gson = Gson()

    override suspend fun login(
        username: String,
        password: String
    ): Result<User> {
        return try {
            println("Username passed in is: ${username} with ${password} password}")
            val resp = authApiService.login(AuthApiService.LoginRequest(username, password))
            println("Did we get a response??? ${resp.body()}")
            if (resp.isSuccessful){
                val token = resp.body()?.token ?: return Result.failure(Exception("No Token Provided"))
                val userResponse = userService.getCurrent("Bearer ${token}")
                if (userResponse.isSuccessful){
                    val userReturn = userResponse.body()!!
                    println("THE LOCAL USER IS ABOUT TO BE SAVED I THINK AND IT IS ${userReturn}")
                    logout()
                    val (firstName, lastName) = extractFirstAndLastName(userReturn.name)
                    userRepository.insertUser(User(
                        email = userReturn.email.toString(),
                        id = userReturn.id?.toInt() ?: 0,
                        firstname =firstName,
                        lastname = lastName,
                        role = userReturn.role.toString(),
                        token = token
                    ))
                    if (userReturn.role == "SELLER"){
                        userReturn.properties?.forEach { p -> savePropertiesSeller(Property(
                            id = p.id,
                            title = p.title,
                            location = p.location,
                            price = p.price.toInt(),
                            beds = p.beds ?: (2..5).random(),
                            baths =  p.bathrooms ?: (1..3).random(),
                            area = p.area?.toString() ?: "4000",
                            imageUrl = "",
                            description = p.description,
                            sellerId = p.sellerId
                        ))
                        }
                        println("Seeing things real quick ${userReturn.sellingAppointments}")
                        userReturn.sellingAppointments?.forEach { s ->
                            appointmentRepository.saveAppointment(mapFromSellerNetwork(s))
                        }
                    }
                    userReturn.bookedAppointments?.forEach { b ->
                        appointmentRepository.saveAppointment(mapFromBuyerNetwork(b))
                    }
                    return (Result.success(User(
                        email = userReturn.email.toString(),
                        id = userReturn.id?.toInt() ?: 0,
                        firstname = firstName,
                        lastname = lastName,
                        role = userReturn.role.toString(),
                        token = token
                    )))
                } else {
                    Result.failure(Exception("Login successful but user data is null"))
                }
            }else {
                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
                val errorMessage = "Login failed with code: ${resp.code()}, message: $errorBody"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

    }

    override suspend fun signup(
        username: String,
        email: String,
        password: String,
        role: String
    ): Result<User> {
        return try {
            val resp = authApiService.signup((AuthApiService.SignupRequest(username, email, password, role)))
            println("Did we get a response??? ${resp.body()}")
            if (resp.isSuccessful){
                println("response looks like this ${resp.body()}")
                val token = resp.body()?.token ?: return Result.failure(Exception("No Token Provided"))
                val userResponse = userService.getCurrent("Bearer ${token}")
                if (userResponse.isSuccessful){
                    val userReturn = userResponse.body()!!
                    println("returned user is then: $userReturn")
                    val (firstName, lastName) = extractFirstAndLastName(userReturn.name)
                    val user = User(
                        email = userReturn.email.toString(),
                        id = userReturn.id?.toInt() ?: 0,
                        firstname = firstName,
                        lastname = lastName,
                        role = userReturn.role.toString(),
                        token = token
                    )
                    logout()
                    userRepository.insertUser(user)
                    return (Result.success(user))
                }
                else {
                    Result.failure(Exception("Signup successful but user data is null"))
                }
            }else {
                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
                val errorMessage = "Login failed with code: ${resp.code()}, message: $errorBody"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            println("Removing properties and user stuff")
            userRepository.deleteUser()
            propertyDao.clearProperties()
            appointmentRepository.deleteAppointments()
        } catch (e: Exception) {
            println(e)
        }
    }


    override suspend fun savePropertiesSeller(property: Property) {
        println("Saving property: $property")
        propertyDao.insert(property)
    }

    private fun extractFirstAndLastName(fullName: String?): Pair<String, String> {
        val nameParts = fullName?.split(" ") ?: emptyList()
        val firstName = nameParts.getOrNull(0) ?: ""
        val lastName = nameParts.getOrNull(1) ?: ""
        return Pair(firstName, lastName)
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



//
//
//import com.example.myapp.data.api.AuthApiService
////import com.example.propSync.data.api.RealAuthAPiService
//import com.example.myapp.data.api.UserService
//import com.example.myapp.data.local.PropertyDAO
//import com.example.myapp.data.model.Property
//import com.example.myapp.data.model.User
//import javax.inject.Inject
//
//interface AuthRepository {
//    suspend fun login(username: String, password: String): Result<User>
//    suspend fun signup(username: String, email: String, password: String, role: String): Result<User>
//    suspend fun logout()
//    suspend fun savePropertiesSeller(property: Property)
//}
//
//
//class AuthRepositoryImpl @Inject constructor(
//    private val authApiService: AuthApiService,
//    private val userRepository: UserRepository,
//    private val userService: UserService,
//    private val propertyDao: PropertyDAO // will change to propertyRepo
//): AuthRepository
//{
//
//    override suspend fun login(
//        username: String,
//        password: String
//    ): Result<User> {
//        return try {
//            println("Username passed in is: ${username} with ${password} password}")
//            val resp = authApiService.login(AuthApiService.LoginRequest(username, password))
//            println("Did we get a response??? ${resp.body()}")
//            if (resp.isSuccessful){
//                val token = resp.body()?.token ?: return Result.failure(Exception("No Token Provided"))
//                val userResponse = userService.getCurrent("Bearer ${token}")
//                if (userResponse.isSuccessful){
//                    val userReturn = userResponse.body()!!
//                    println("THE LOCAL USER IS ABOUT TO BE SAVED I THINK AND IT IS ${userReturn}")
//                    logout()
//                    userRepository.insertUser(User(
//                        email = userReturn.email.toString(),
//                        id = userReturn.id?.toInt() ?: 0,
//                        firstname = userReturn.name.toString().split(" ")[0],
//                        lastname = userReturn.name.toString().split(" ")[1],
//                        role = userReturn.role.toString(),
//                        token = token
//                    ))
//                    if (userReturn.role == "SELLER"){
//                        userReturn.properties?.forEach { p -> savePropertiesSeller(Property(
//                            id = p.id,
//                            title = p.title,
//                            location = p.location,
//                            price = p.price,
//                            beds = (2..5).random(),
//                            baths = (1..3).random(),
//                            area = "45000",
//                            imageUrl = "",
//                            description = p.description,
//                            sellerId = p.sellerId
//                        ))
//                        }
//                    }
//                    return (Result.success(User(
//                        email = userReturn.email.toString(),
//                        id = userReturn.id?.toInt() ?: 0,
//                        firstname = userReturn.name.toString().split(" ")[0],
//                        lastname = userReturn.name.toString().split(" ")[1],
//                        role = userReturn.role.toString(),
//                        token = token
//                    )))
//                } else {
//                    Result.failure(Exception("Login successful but user data is null"))
//                }
//            }else {
//                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
//                val errorMessage = "Login failed with code: ${resp.code()}, message: $errorBody"
//                Result.failure(Exception(errorMessage))
//            }
//        } catch (e: Exception) {
//            return Result.failure(e)
//        }
//
//    }
//
//    override suspend fun signup(
//        username: String,
//        email: String,
//        password: String,
//        role: String
//    ): Result<User> {
//        return try {
//            val resp = authApiService.signup((AuthApiService.SignupRequest(username, email, password, role)))
//            if (resp.isSuccessful){
//                val user = resp.body()
//                if (user != null){
//                    userRepository.insertUser(user)
//                    return (Result.success(user))
//                } else {
//                    Result.failure(Exception("Signup successful but user data is null"))
//                }
//            }else {
//                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
//                val errorMessage = "Login failed with code: ${resp.code()}, message: $errorBody"
//                Result.failure(Exception(errorMessage))
//            }
//        } catch (e: Exception) {
//            return Result.failure(e)
//        }
//    }
//
//    override suspend fun logout() {
//        println("Removing properties and user stuff")
//        userRepository.deleteUser()
//        propertyDao.clearProperties()
//    }
//
//
//    override suspend fun savePropertiesSeller(property: Property) {
//        println("Saving property: $property")
//        propertyDao.insert(property)
//    }
//
//
//}
//


