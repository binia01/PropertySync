package com.example.myapp.data.repository

import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.Request.AddPropertyRequest
import com.example.myapp.data.model.Response.AddPropertyResponse
import com.example.myapp.data.model.ReturnProperty
import com.example.myapp.ui.screen.property.NetworkResult
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
    suspend fun createProperty(token: String, property: AddPropertyRequest): NetworkResult<AddPropertyResponse>
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
    ): Result<ReturnProperty>

    fun getPropertyById(token: String, propertyId: Int): Flow<Result<Property?>>



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

    override suspend fun createProperty(token: String,property: AddPropertyRequest): NetworkResult<AddPropertyResponse> {
        return try {
            val response = propertyApiService.addProperty("Bearer $token", property)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error("Empty response")
            } else {
                NetworkResult.Error(response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
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

    override fun getPropertyById(token: String, propertyId: Int): Flow<Result<Property?>> = flow {
        runCatching {
            val response = propertyApiService.getPropertyById(token, propertyId)
            if (response.isSuccessful) {
                Result.success(response.body()?.toProperty())
            } else {
                Result.failure(Exception("Failed to fetch property with ID $propertyId: ${response.code()} - ${response.message()}"))
            }
        }.onSuccess { result -> emit(result) }
            .onFailure { error -> emit(Result.failure(error)) }
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
