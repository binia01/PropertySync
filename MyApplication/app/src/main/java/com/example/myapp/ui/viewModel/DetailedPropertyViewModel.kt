package com.example.myapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Property
import com.example.myapp.data.model.Request.CreateAppointmentRequest
import com.example.myapp.data.repository.AppointmentRepository
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedPropertyViewModel  @Inject constructor (
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
):  ViewModel(){

    private val _propertyLoading = MutableStateFlow(false)
    val propertyLoading: StateFlow<Boolean> = _propertyLoading

    private val _selectedProperty = MutableStateFlow<Property?>(null)
    val selectedProperty: StateFlow<Property?> = _selectedProperty


    private val _createAppointmentResult = MutableStateFlow<Result<Unit>?>(null)

    fun createAppointment(propertyId: Int, date: String, time: String, sellerId: Int?) {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    if (!authToken.isNullOrBlank()) {
                        try {
                            val formattedDate = "${date}T00:00:00.000Z"
                            val formattedStartTime = "${date}T${time}:00.000Z"

                            val request = CreateAppointmentRequest(
                                propertyId = propertyId,
                                Date = formattedDate,
                                startTime = formattedStartTime,
                            )
                            val result = appointmentRepository.createAppointment(authToken, request)
                            println(result)

                        } catch (e: Exception) {
                            _createAppointmentResult.value = Result.failure(e)
                            println("Error creating appointment: ${e.localizedMessage ?: "Unknown error"}")
                        }
                    } else {
                        _createAppointmentResult.value = Result.failure(Exception("Authentication token is missing"))
                        println("Error creating appointment: Authentication token is missing")
                    }
                }
        }
    }

    fun getPropertyDetails(propertyId: Int?) {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    if (!authToken.isNullOrBlank()) {
                        _propertyLoading.value = true
                        if (propertyId!= null){
                            propertyRepository.getPropertyById("Bearer $authToken", propertyId)
                                .collectLatest { result ->
                                    _propertyLoading.value = false
                                    if (result.isSuccess) {
                                        _selectedProperty.value = result.getOrNull()
                                        println("Successfully fetched property details: ${_selectedProperty.value}")
                                    } else {

                                        println("Error fetching property details: result.exceptionOrNull()?.localizedMessage ?: \"Failed to fetch property details\"")
                                        _selectedProperty.value = null
                                    }
                                }
                        }else{
                            println("Error: Property ID is null")
                        }
                    } else {
                        println("Error: User token not available to fetch property details")
                        _selectedProperty.value = null
                    }
                }
        }
    }
}


