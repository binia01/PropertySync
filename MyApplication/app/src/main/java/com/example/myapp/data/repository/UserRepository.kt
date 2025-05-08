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
    suspend fun updateUser(firstname: String?, email: String?, lastname: String?): Result<Unit>
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

    override suspend fun updateUser(firstname: String?, email: String?, lastname: String?): Result<Unit> {
        return try {
            val currentUser = getUser().firstOrNull()

            if (currentUser?.token != null) {
                val updateRequest = UserService.UpdateRequest(
                    email = email ?: currentUser.email,
                    firstname = firstname ?: currentUser.firstname,
                    lastname = lastname ?: currentUser.lastname
                )

                val res = userApiService.update("Bearer ${currentUser.token}", updateRequest)

                if (res.isSuccessful) {
                    val userReturn = res.body()?.userRet
                    if (userReturn != null) {
                        val updatedUserLocal = User(
                            id = userReturn.id?.toInt() ?: currentUser.id,
                            email = userReturn.email ?: currentUser.email,
                            firstname = firstname ?: currentUser.firstname,
                            lastname = lastname ?: currentUser.lastname,
                            role = currentUser.role,
                            token = currentUser.token
                        )
                        insertUser(updatedUserLocal)
                        return Result.success(Unit)
                    } else {
                        return Result.failure(Exception("Successful response but user data is null"))
                    }
                } else {
                    return Result.failure(Exception("Backend Error: ${res.errorBody()?.string() ?: res.message()}"))
                }
            } else {
                return Result.failure(Exception("User not logged in or token is null"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}
