package com.example.myapp.data.repository

import com.example.myapp.data.api.AuthApiService
import com.example.myapp.data.api.UserService
import com.example.myapp.data.model.User
import javax.inject.Inject

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun signup(username: String, email: String, password: String, role: String): Result<User>
    suspend fun logout()
}

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository,
    private val userService: UserService,
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<User> {
        return try {
            println("Logging in user: $username")
            val resp = authApiService.login(AuthApiService.LoginRequest(username, password))
            if (resp.isSuccessful) {
                val token = resp.body()?.token ?: return Result.failure(Exception("No Token Provided"))
                val userResponse = userService.getCurrent("Bearer $token")
                if (userResponse.isSuccessful) {
                    val userReturn = userResponse.body()!!
                    println("Login successful, user: $userReturn")
                    val (firstName, lastName) = extractFirstAndLastName(userReturn.name)
                    val user = User(
                        email = userReturn.email.toString(),
                        id = userReturn.id?.toInt() ?: 0,
                        firstname = firstName,
                        lastname = lastName,
                        role = userReturn.role.toString(),
                        token = token
                    )
                    userRepository.insertUser(user) // Store user info (including token) locally for session
                    return Result.success(user)
                } else {
                    Result.failure(Exception("Login successful but failed to fetch user details"))
                }
            } else {
                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
                val errorMessage = "Login failed: ${resp.code()} - $errorBody"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(
        username: String,
        email: String,
        password: String,
        role: String
    ): Result<User> {
        return try {
            println("Signing up user: $username with email: $email and role: $role")
            val resp = authApiService.signup(AuthApiService.SignupRequest(username, email, password, role))
            if (resp.isSuccessful) {
                val token = resp.body()?.token ?: return Result.failure(Exception("No Token Provided"))
                val userResponse = userService.getCurrent("Bearer $token")
                if (userResponse.isSuccessful) {
                    val userReturn = userResponse.body()!!
                    println("Signup successful, user: $userReturn")
                    val (firstName, lastName) = extractFirstAndLastName(userReturn.name)
                    val user = User(
                        email = userReturn.email.toString(),
                        id = userReturn.id?.toInt() ?: 0,
                        firstname = firstName,
                        lastname = lastName,
                        role = userReturn.role.toString(),
                        token = token
                    )
                    userRepository.insertUser(user) // Store user info (including token) locally for session
                    return Result.success(user)
                } else {
                    Result.failure(Exception("Signup successful but failed to fetch user details"))
                }
            } else {
                val errorBody = resp.errorBody()?.string() ?: "Unknown error"
                val errorMessage = "Signup failed: ${resp.code()} - $errorBody"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            println("Logging out: Clearing local user data")
            userRepository.deleteUser() // Clear the local User entity (including token)
        } catch (e: Exception) {
            println("Error during logout: $e")
        }
    }

    private fun extractFirstAndLastName(fullName: String?): Pair<String, String> {
        val nameParts = fullName?.split(" ") ?: emptyList()
        val firstName = nameParts.getOrNull(0) ?: ""
        val lastName = nameParts.getOrNull(1) ?: ""
        return Pair(firstName, lastName)
    }
}

