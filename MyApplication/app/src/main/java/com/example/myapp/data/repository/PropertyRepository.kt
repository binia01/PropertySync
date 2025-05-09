package com.example.myapp.data.repository


import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.local.PropertyDAO
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.ReturnProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


interface PropertyRepository {
    suspend fun getProperties(token: String): Result<List<Property>>
    suspend fun insertProperty(property: Property)
    fun getLocalProps(): Flow<List<Property>>
    suspend fun deleteProperty(id: Int)
    suspend fun updateProperty(propId: String,
                               token: String,
                               title: String?,
                               description: String?,
                               area: String?,
                               beds: String?,
                               baths: String?,
                               location: String?,
                               price: String?): Result<Any>
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
                                beds = p.beds ?: (2..5).random(),
                                baths =  p.bathrooms ?: (1..3).random(),
                                area = p.area?.toString() ?: "4000",
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

    override suspend fun updateProperty(propId: String,
                                        token: String,
                                        title: String?,
                                        description: String?,
                                        area: String?,
                                        beds: String?,
                                        baths: String?,
                                        location: String?,
                                        price: String? ): Result<Any> {
        return try {
            val property = propertyDAO.getPropertyById(propId.toString()).firstOrNull()
            if (property != null) {
                val resp = propertyApiService.updateProperty(
                    id = propId,
                    token = token,
                    updateProperty = PropertyService.UpdateProperty(
                        title = title ?: property.title,
                        description = description ?: property.description,
                        area = area ?: property.area,
                        beds = (beds ?: property.beds).toString(),
                        baths = (baths ?: property.baths).toString(),
                        location = location ?: property.location,
                        price = (price ?: property.price).toString()
                    )
                )
                if (resp.isSuccessful){
                    println(resp.body()!!)
                    Result.success("YIPPI")
                }else{
                    println("Eroor with: ${resp}")
                    Result.failure(Exception("Failed to update property: ${resp.errorBody()?.string()}"))
                }
            }else{
                Result.failure(Exception("Failed to find a property: $property"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }


    }

}
