package com.example.myapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Property
import com.example.myapp.data.repository.AppointmentRepository
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _appointments = MutableStateFlow<List<AppointmentEntity>?>(null)
    val appointments: StateFlow<List<AppointmentEntity>?> = _appointments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _selectedProperty = MutableStateFlow<Property?>(null)
    val selectedProperty: StateFlow<Property?> = _selectedProperty

    private val _propertyLoading = MutableStateFlow(false)
    val propertyLoading: StateFlow<Boolean> = _propertyLoading

    private val _propertyError = MutableStateFlow<String?>(null)
    val propertyError: StateFlow<String?> = _propertyError

    private val _updateLoading = MutableStateFlow(false)
    val updateLoading: StateFlow<Boolean> = _updateLoading

    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> = _updateError

    private val _updateStatusLoading = MutableStateFlow(false)
    val updateStatusLoading: StateFlow<Boolean> = _updateStatusLoading

    private val _updateStatusError = MutableStateFlow<String?>(null)
    val updateStatusError: StateFlow<String?> = _updateStatusError

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole : StateFlow<String?> = _userRole

    init {
        println("AppointmentViewModel initialized") // Log when ViewModel is created
        refreshAppointments()
        observeAppointmentUpdates()
    }



    fun refreshAppointments() {
        viewModelScope.launch {
            userRepository.getUser()
                .collectLatest { user ->
                    println("refreshAppointments: Token received: ${user?.token}") // Log the token
                    val authToken = user?.token
                    if (!authToken.isNullOrBlank()) {
                        _isLoading.value = true
                        _userRole.value = user.role
                        println("refreshAppointments: Calling appointmentRepository.refreshAppointments...") // Log before repository call
                        appointmentRepository.refreshAppointments("Bearer $authToken")
                            .onSuccess { fetchedAppointments ->
                                _isLoading.value = false
                                _appointments.value = fetchedAppointments
                                _errorMessage.value = null
                                println("refreshAppointments: Success! Fetched ${fetchedAppointments.size} appointments") // Log success and count
                                fetchedAppointments.forEach { println("Appointment: $it") } // Log each appointment
                            }
                            .onFailure { error ->
                                _isLoading.value = false
                                _appointments.value = null
                                _errorMessage.value = error.localizedMessage ?: "Failed to refresh appointments"
                                println("refreshAppointments: Failure! Error: ${_errorMessage.value}") // Log failure
                            }
                    } else {
                        _appointments.value = null
                        _errorMessage.value = "User token not available"
                        println("refreshAppointments: Token is null or blank") // Log token issue
                    }
                }
        }
    }
    fun getPropertyDetails(propertyId: Int): Flow<Result<Property>> =
    userRepository.getUser()
    .map { user ->
        println("getPropertyDetails: User token flow emitted: $user")
        user?.token
    }
    .flatMapLatest { authToken ->
        if (!authToken.isNullOrBlank()) {
            println("getPropertyDetails: Fetching property with token: $authToken, id: $propertyId")
            propertyRepository.getPropertyById("Bearer $authToken", propertyId)
                .map { result ->
                    println("getPropertyDetails: Repository returned result for id $propertyId: $result")
                    result.map { returnProperty ->
                        println("getPropertyDetails: Mapping ReturnProperty ($returnProperty) to Property")
                        returnProperty as Property // Assuming direct cast for now
                    }
                }
        } else {
            println("getPropertyDetails: User token is null or blank.")
            flowOf(Result.failure(Exception("User token not available")))
        }
    }
    fun updateAppointment(id: Int, date: String, startTime: String) {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    if (!authToken.isNullOrBlank()) {
                        _updateLoading.value = true
                        _updateError.value = null
                        appointmentRepository.updateAppointment("Bearer $authToken", id, date, startTime)
                            .onSuccess {
                                _updateLoading.value = false
                                refreshAppointments() // Refresh the list after successful update
                            }
                            .onFailure { error ->
                                _updateLoading.value = false
                                _updateError.value = error.localizedMessage ?: "Failed to update appointment"
                            }
                    } else {
                        _updateError.value = "User token not available"
                    }
                }
        }
    }
    private fun observeAppointmentUpdates() {
        viewModelScope.launch {
            appointmentRepository.appointmentUpdatedFlow.collect {
                refreshAppointments() // reload on update
            }
        }
    }

    fun updateAppointmentStatus(id: Int, status: String) {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    if (!authToken.isNullOrBlank()) {
                        _updateStatusLoading.value = true
                        _updateStatusError.value = null

                        appointmentRepository.updateAppointmentStatus("Bearer $authToken", id, status, (userRole.value == "SELLER"))
                            .onSuccess {
                                _updateStatusLoading.value = false
                                refreshAppointments()
                            }
                            .onFailure { error ->
                                _updateStatusLoading.value = false
                                _updateStatusError.value = error.localizedMessage ?: "Failed to update appointment status"
                            }
                    } else {
                        _updateStatusError.value = "User token not available"
                    }
                }
        }
    }
}