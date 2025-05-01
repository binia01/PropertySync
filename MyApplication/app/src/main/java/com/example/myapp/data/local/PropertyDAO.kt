package com.example.myapp.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapp.data.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDAO {
    @Query("SELECT * FROM property")
    fun getProperties(): Flow<List<Property>> // Use Flow for reactive updates

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: Property)

    @Query("DELETE FROM property")
    suspend fun clearProperties()
}