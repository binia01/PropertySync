package com.example.myapp.data.repository


import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.Request.AddPropertyRequest
import com.example.myapp.data.model.Response.AddPropertyResponse
import com.example.myapp.ui.screen.property.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


interface PropertyRepository {
    suspend fun getProperties(token: String): Result<List<Property>>
    suspend fun createProperty(token: String, property: AddPropertyRequest): NetworkResult<AddPropertyResponse>
    fun getLocalProps(): Flow<List<Property>>
    suspend fun deleteProperty(id: Int)
    suspend fun updateProperty(id: Int)
    suspend fun insertProperty(property: Property)
}

class PropertyRepoImpl @Inject constructor(
    private val propertyDAO: PropertyDAO,
    private val propertyApiService: PropertyService,
): PropertyRepository {
    override suspend fun getProperties(token: String): Result<List<Property>> {
        return try {
            val propRes = propertyApiService.getProperties(token)
            if (propRes.isSuccessful) {
                val propertyReturn = propRes.body()
                if (propertyReturn != null) {
                    val propList = propertyReturn
                        .map { p ->
                            Property(
                                id = p.id,
                                title = p.title,
                                location = p.location,
                                price = p.price.toInt(),
                                beds =  (2..5).random(),
                                baths =  (1..3).random(),
                                area = "4000",
                                imageUrl = "",
                                description = p.description,
                                sellerId = p.sellerId
                            )
                        }
                    propList.forEach { p -> insertProperty(p) }
                    Result.success(propList)
                } else {
                    println(propertyReturn)
                    Result.failure(Exception("Received empty or malformed property data"))
                }
            } else {
                Result.failure(Exception("Failed to fetch properties: ${propRes.code()} - ${propRes.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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

    override suspend fun insertProperty(property: Property) {
        propertyDAO.insert(property)
    }

    override fun getLocalProps(): Flow<List<Property>> {
        return propertyDAO.getProperties()
    }

    override suspend fun deleteProperty(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProperty(id: Int) {
        TODO("Not yet implemented")
    }

}
