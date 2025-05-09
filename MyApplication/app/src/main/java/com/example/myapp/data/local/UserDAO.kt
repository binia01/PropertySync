//package com.example.myapp.data.local;
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.myapp.data.model.User
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface UserDao {
//    @Query("SELECT * FROM user LIMIT 1")
//    fun getUser(): Flow<User?> // Use Flow for reactive updates
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(user: User)
//
//    @Query("DELETE FROM user")
//    suspend fun clearUser()
//}
//
