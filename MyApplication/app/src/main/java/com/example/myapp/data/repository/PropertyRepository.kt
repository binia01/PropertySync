package com.example.myapp.data.repository


import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.model.Property
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface PropertyRepository {
    suspend fun getProperties(token: String): Result<List<Property>>
    suspend fun insertProperty(property: Property)
    fun getLocalProps(): Flow<List<Property>>
    suspend fun deleteProperty(id: Int)
    suspend fun updatePropterty(id: Int)
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


    override suspend fun insertProperty(property: Property) {
        propertyDAO.insert(property)
    }

    override fun getLocalProps(): Flow<List<Property>> {
        return propertyDAO.getProperties()
    }

    override suspend fun deleteProperty(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePropterty(id: Int) {
        TODO("Not yet implemented")
    }

}
