package com.example.myapp.data.repository

import com.example.myapp.data.api.AuthApiService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.local.UserDao
import com.example.myapp.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun insertUser(user: User)
    suspend fun deleteUser()
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
        // make network call to delete from the backend
        // then clear the user from local.
        userDao.clearUser()
    }

    override suspend fun updateUser(user: User) {
        //TODO
    }


}
