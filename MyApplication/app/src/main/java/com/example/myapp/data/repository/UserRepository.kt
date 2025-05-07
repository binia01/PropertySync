package com.example.myapp.data.repository

import com.example.myapp.data.api.AuthApiService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.local.UserDao
import com.example.myapp.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun insertUser(user: User)
    suspend fun deleteUser()
    suspend fun deleteUserAccount() : Result<Unit>
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserService
): UserRepository {
    override fun getUser(): Flow<User?> {
        return userDao.getUser()
    }

    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun deleteUser() {
        try {
            userDao.clearUser()
            println("cleared?")
        } catch (e: Exception) {
            println(e)
        }
    }
    override suspend fun deleteUserAccount(): Result<Unit> {
        return try {
            val currentUserFlow = getUser()
            val currentUser = currentUserFlow.firstOrNull()

            if (currentUser?.token != null) {
                val response = userApiService.delete("Bearer ${currentUser.token}")
                if (response.isSuccessful) {
                    deleteUser()
                    Result.success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    val errorMessage = "Failed to delete account: ${response.code()}, message: $errorBody"
                    Result.failure(Exception(errorMessage))
                }
            } else {
                Result.failure(Exception("No user logged in to delete"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User) {
        //TODO
    }


}
