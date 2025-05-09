package com.example.myapp.data.repository

import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.ReturnProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.runCatching

interface PropertyRepository {
    fun getProperties(token: String): Flow<Result<List<Property>>>
    suspend fun deleteProperty(id: Int, token: String): Result<Unit>
    suspend fun updateProperty(
        propId: String,
        token: String,
        title: String?,
        description: String?,
        area: String?,
        beds: String?,
        baths: String?,
        location: String?,
        price: String?
    ): Result<ReturnProperty> // Expecting ReturnProperty from update

    val propertyUpdatedFlow: kotlinx.coroutines.flow.SharedFlow<Unit>
}

@Singleton
class PropertyRepoImpl @Inject constructor(
    private val propertyApiService: PropertyService,
) : PropertyRepository {
    private val _propertyUpdatedFlow = MutableSharedFlow<Unit>()
    override val propertyUpdatedFlow = _propertyUpdatedFlow.asSharedFlow() // Expose as SharedFlow

    override fun getProperties(token: String): Flow<Result<List<Property>>> = flow {
        runCatching {
            val propRes = propertyApiService.getProperties(token)
            if (propRes.isSuccessful) {
                val propertyReturnList = propRes.body()
                if (propertyReturnList != null) {
                    val propList = propertyReturnList.map { it.toProperty() }
                    Result.success(propList)
                } else {
                    Result.failure(Exception("Received empty property data"))
                }
            } else {
                Result.failure(Exception("Failed to fetch properties: ${propRes.code()} - ${propRes.message()}"))
            }
        }.onSuccess { result -> emit(result) }
            .onFailure { error -> emit(Result.failure(error)) }
    }

    override suspend fun deleteProperty(id: Int, token: String): Result<Unit> = runCatching {
        val response = propertyApiService.deleteProperty(id.toString(), token)
        if (response.isSuccessful) {
            _propertyUpdatedFlow.emit(Unit)
            Result.success(Unit) // Emit success for consistency
        } else {
            throw Exception("Failed to delete property: ${response.code()} - ${response.message()} - ${response.errorBody()?.string()}")
        }
    }

    override suspend fun updateProperty(
        propId: String,
        token: String,
        title: String?,
        description: String?,
        area: String?,
        beds: String?,
        baths: String?,
        location: String?,
        price: String?
    ): Result<ReturnProperty> = runCatching {
        val resp = propertyApiService.updateProperty(
            id = propId,
            token = token,
            updateProperty = PropertyService.UpdateProperty(
                title = title,
                description = description,
                area = area?.toIntOrNull(),
                beds = beds?.toIntOrNull(),
                baths = baths?.toIntOrNull(),
                location = location,
                price = price?.toIntOrNull()
            )
        )
        if (resp.isSuccessful && resp.body() != null) {
            _propertyUpdatedFlow.emit(Unit)
            resp.body()!!.property // Access the ReturnProperty from the EditResponse
        } else {
            throw Exception("Failed to update property: ${resp.code()} - ${resp.message()} - ${resp.errorBody()?.string()}")
        }
    }

    private fun ReturnProperty.toProperty(): Property {
        return Property(
            id = this.id,
            title = this.title,
            location = this.location,
            price = this.price.toInt(),
            beds = this.beds ?: 0,
            baths = this.bathrooms ?: 0,
            area = this.area?.toString() ?: "",
            imageUrl = "", // You'll likely need to handle this differently
            description = this.description,
            sellerId = this.sellerId
        )
    }
}

//
//import com.example.myapp.data.api.PropertyService
//import com.example.myapp.data.model.Property
//import com.example.myapp.data.model.ReturnProperty
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlin.Result
//import kotlin.runCatching
//
//interface PropertyRepository {
//    fun getProperties(token: String): Flow<Result<List<Property>>>
//    suspend fun deleteProperty(id: Int, token: String): Result<Unit>
//    suspend fun updateProperty(
//        propId: String,
//        token: String,
//        title: String?,
//        description: String?,
//        area: String?,
//        beds: String?,
//        baths: String?,
//        location: String?,
//        price: String?
//    ): Result<ReturnProperty> // Expecting ReturnProperty from update
//
//    val propertyUpdatedFlow: kotlinx.coroutines.flow.SharedFlow<Unit>
//}
//
//@Singleton
//class PropertyRepoImpl @Inject constructor(
//    private val propertyApiService: PropertyService,
//) : PropertyRepository {
//    private val _propertyUpdatedFlow = MutableSharedFlow<Unit>()
//    override val propertyUpdatedFlow = _propertyUpdatedFlow.asSharedFlow() // Expose as SharedFlow
//
//    override fun getProperties(token: String): Flow<Result<List<Property>>> = flow {
//        runCatching {
//            val propRes = propertyApiService.getProperties(token)
//            if (propRes.isSuccessful) {
//                val propertyReturnList = propRes.body()
//                if (propertyReturnList != null) {
//                    val propList = propertyReturnList.map { it.toProperty() }
//                    Result.success(propList)
//                } else {
//                    Result.failure(Exception("Received empty property data"))
//                }
//            } else {
//                Result.failure(Exception("Failed to fetch properties: ${propRes.code()} - ${propRes.message()}"))
//            }
//        }.onSuccess { result -> emit(result) }
//            .onFailure { error -> emit(Result.failure(error)) }
//    }
//
//    override suspend fun deleteProperty(id: Int, token: String): Result<Unit> = runCatching {
//        val response = propertyApiService.deleteProperty(id.toString(), token)
//        if (response.isSuccessful) {
//            _propertyUpdatedFlow.emit(Unit)
//            Result.success(Unit) // Emit success for consistency
//        } else {
//            throw Exception("Failed to delete property: ${response.code()} - ${response.message()} - ${response.errorBody()?.string()}")
//        }
//    }
//
//    override suspend fun updateProperty(
//        propId: String,
//        token: String,
//        title: String?,
//        description: String?,
//        area: String?,
//        beds: String?,
//        baths: String?,
//        location: String?,
//        price: String?
//    ): Result<ReturnProperty> = runCatching {
//        val resp = propertyApiService.updateProperty(
//            id = propId,
//            token = token,
//            updateProperty = PropertyService.UpdateProperty(
//                title = title,
//                description = description,
//                area = area?.toIntOrNull(),
//                beds = beds?.toIntOrNull(),
//                baths = baths?.toIntOrNull(),
//                location = location,
//                price = price?.toIntOrNull()
//            )
//        )
//        if (resp.isSuccessful && resp.body() != null) {
//            _propertyUpdatedFlow.emit(Unit)
//            resp.body()!! // Assuming the direct response body is ReturnProperty
//        } else {
//            throw Exception("Failed to update property: ${resp.code()} - ${resp.message()} - ${resp.errorBody()?.string()}")
//        }
//    }
//
//    private fun ReturnProperty.toProperty(): Property {
//        return Property(
//            id = this.id,
//            title = this.title,
//            location = this.location,
//            price = this.price.toInt(),
//            beds = this.beds ?: 0,
//            baths = this.bathrooms ?: 0,
//            area = this.area?.toString() ?: "",
//            imageUrl = "", // You'll likely need to handle this differently
//            description = this.description,
//            sellerId = this.sellerId
//        )
//    }
//}
//
//



















//
//@Singleton
//class PropertyRepoImpl @Inject constructor(
//    private val propertyApiService: PropertyService,
//) : PropertyRepository {
//    private val _propertyUpdatedFlow = MutableSharedFlow<Unit>()
//    val propertyUpdatedFlow = _propertyUpdatedFlow.asSharedFlow() // Expose as SharedFlow
//
//    override fun getProperties(token: String): Flow<Result<List<Property>>> = flow {
//        runCatching {
//            val propRes = propertyApiService.getProperties(token)
//            if (propRes.isSuccessful) {
//                val propertyReturn = propRes.body()
//                if (propertyReturn != null) {
//                    val propList = propertyReturn.map { it.toProperty() }
//                    Result.success(propList)
//                } else {
//                    Result.failure(Exception("Received empty property data"))
//                }
//            } else {
//                Result.failure(Exception("Failed to fetch properties: ${propRes.code()} - ${propRes.message()}"))
//            }
//        }.onSuccess { result -> emit(result) }
//            .onFailure { error -> emit(Result.failure(error)) }
//    }
//
//    override suspend fun deleteProperty(id: Int, token: String): Result<Unit> = runCatching {
//        val response = propertyApiService.deleteProperty(id.toString(), token)
//        if (response.isSuccessful) {
//            _propertyUpdatedFlow.emit(Unit)
//            Result.success(Unit) // Emit success for consistency
//        } else {
//            throw Exception("Failed to delete property: ${response.code()} - ${response.message()}")
//        }
//    }
//
//    override suspend fun updateProperty(
//        propId: String,
//        token: String,
//        title: String?,
//        description: String?,
//        area: String?,
//        beds: String?,
//        baths: String?,
//        location: String?,
//        price: String?
//    ): Result<ReturnProperty> = runCatching {
//        val resp = propertyApiService.updateProperty(
//            id = propId,
//            token = token,
//            updateProperty = PropertyService.UpdateProperty(
//                title = title,
//                description = description,
//                area = area?.toIntOrNull(),
//                beds = beds?.toIntOrNull(),
//                baths = baths?.toIntOrNull(),
//                location = location,
//                price = price?.toIntOrNull()
//            )
//        )
//        if (resp.isSuccessful && resp.body() != null) {
//            _propertyUpdatedFlow.emit(Unit)
//            resp.body()!!.property // Access the ReturnProperty from the EditResponse
//        } else {
//            throw Exception("Failed to update property: ${resp.code()} - ${resp.message()} - ${resp.errorBody()?.string()}")
//        }
//    }
//}
//
//// Extension function to map ReturnProperty (DTO) to Property (domain model)
//private fun ReturnProperty.toProperty(): Property {
//    return Property(
//        id = this.id,
//        title = this.title,
//        location = this.location,
//        price = this.price.toInt(),
//        beds = this.beds ?: 0,
//        baths = this.bathrooms ?: 0,
//        area = this.area?.toString() ?: "",
//        imageUrl = "", // You'll likely need to handle this differently
//        description = this.description,
//        sellerId = this.sellerId
//    )
//}
////import com.example.myapp.data.api.PropertyService
////import com.example.myapp.data.local.PropertyDAO
////import com.example.myapp.data.model.Property
////import com.example.myapp.data.model.ReturnProperty
////import kotlinx.coroutines.flow.Flow
////import kotlinx.coroutines.flow.MutableSharedFlow
////import kotlinx.coroutines.flow.asSharedFlow
////import kotlinx.coroutines.flow.firstOrNull
////import kotlinx.coroutines.flow.flow
////import javax.inject.Inject
////
////
//interface PropertyRepository {
//    suspend fun getProperties(token: String): Result<List<Property>>
////    suspend fun insertProperty(property: Property)
////    fun getLocalProps(): Flow<List<Property>>
//    suspend fun deleteProperty(id: Int, token: String)
//    suspend fun updateProperty(propId: String,
//                               token: String,
//                               title: String?,
//                               description: String?,
//                               area: String?,
//                               beds: String?,
//                               baths: String?,
//                               location: String?,
//                               price: String?): Result<Any>
//}
//
//class PropertyRepoImpl @Inject constructor(
//    private val propertyDAO: PropertyDAO,
//    private val propertyApiService: PropertyService,
//): PropertyRepository {
//    override suspend fun getProperties(token: String): Result<List<Property>> {
//        return try {
//            val propRes = propertyApiService.getProperties(token)
//            if (propRes.isSuccessful) {
//                val propertyReturn = propRes.body()
//                if (propertyReturn != null) {
//                    val propList = propertyReturn
//                        .map { p ->
//                            Property(
//                                id = p.id,
//                                title = p.title,
//                                location = p.location,
//                                price = p.price.toInt(),
//                                beds = p.beds ?: (2..5).random(),
//                                baths =  p.bathrooms ?: (1..3).random(),
//                                area = p.area?.toString() ?: "4000",
//                                imageUrl = "",
//                                description = p.description,
//                                sellerId = p.sellerId
//                            )
//                        }
//                    propList.forEach { p -> insertProperty(p) }
//                    Result.success(propList)
//                } else {
//                    println(propertyReturn)
//                    Result.failure(Exception("Received empty or malformed property data"))
//                }
//            } else {
//                Result.failure(Exception("Failed to fetch properties: ${propRes.code()} - ${propRes.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//
//    override suspend fun insertProperty(property: Property) {
//        propertyDAO.insert(property)
//    }
//
//    override fun getLocalProps(): Flow<List<Property>> {
//        return propertyDAO.getProperties()
//    }
//
//    override suspend fun deleteProperty(id: Int, token: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun updateProperty(propId: String,
//                                        token: String,
//                                        title: String?,
//                                        description: String?,
//                                        area: String?,
//                                        beds: String?,
//                                        baths: String?,
//                                        location: String?,
//                                        price: String? ): Result<Any> {
//        return try {
//            val property = propertyDAO.getPropertyById(propId.toString()).firstOrNull()
//            if (property != null) {
//                val resp = propertyApiService.updateProperty(
//                    id = propId,
//                    token = token,
//                    updateProperty = PropertyService.UpdateProperty(
//                        title = title ?: property.title,
//                        description = description ?: property.description,
//                        area = (area)?.toIntOrNull() ?: (property.area).toInt(),
//                        beds = (beds)?.toIntOrNull() ?: property.beds,
//                        baths = (baths)?.toIntOrNull() ?: property.baths,
//                        location = location ?: property.location,
//                        price = price?.toIntOrNull() ?: property.price
//                    )
//                )
//                if (resp.isSuccessful){
//                    val p = resp.body()?.property!!
//                    println(p)
//                    insertProperty(Property(
//                        id = p.id,
//                        title = p.title,
//                        location = p.location,
//                        price = p.price.toInt(),
//                        beds = p.beds ?: (2..5).random(),
//                        baths =  p.bathrooms ?: (1..3).random(),
//                        area = p.area?.toString() ?: "4000",
//                        imageUrl = "",
//                        description = p.description,
//                        sellerId = p.sellerId
//                    ))
//                    Result.success(resp.message())
//                }else{
//                    println("Eroor with: ${resp}")
//                    Result.failure(Exception("Failed to update property: ${resp.errorBody()?.string()}"))
//                }
//            }else{
//                Result.failure(Exception("Failed to find a property: $property"))
//            }
//        }catch (e: Exception){
//            Result.failure(Exception("It seems we broke $e"))
//        }
//
//
//    }
//
//}

