package com.example.myapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.repository.AppointmentRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _appointments = MutableStateFlow<List<AppointmentEntity>?>(null)
    val appointments: StateFlow<List<AppointmentEntity>?> = _appointments

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        println("AppointmentViewModel initialized") // Log when ViewModel is created
        refreshAppointments()
    }

    fun refreshAppointments() {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    println("refreshAppointments: Token received: $authToken") // Log the token

                    if (!authToken.isNullOrBlank()) {
                        _isLoading.value = true
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
}

//package com.example.myapp.ui.viewModel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapp.data.model.AppointmentEntity
//import com.example.myapp.data.repository.AppointmentRepository
//import com.example.myapp.data.repository.UserRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AppointmentViewModel @Inject constructor(
//    private val appointmentRepository: AppointmentRepository,
//    private val userRepository: UserRepository
//) : ViewModel() {
//    private val _appointments = MutableStateFlow<List<AppointmentEntity>?>(null)
//    val appointments: StateFlow<List<AppointmentEntity>?> = _appointments
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    init {
//        refreshAppointments()
//    }
//
//    fun refreshAppointments() {
//        viewModelScope.launch {
//            userRepository.getUser()
//                .map { user -> user?.token }
//                .collectLatest { authToken ->
//                    if (!authToken.isNullOrBlank()) {
//                        _isLoading.value = true
//                        appointmentRepository.refreshAppointments("Bearer $authToken")
//                            .onSuccess { fetchedAppointments ->
//                                _isLoading.value = false
//                                _appointments.value = fetchedAppointments
//                                _errorMessage.value = null
//                            }
//                            .onFailure { error ->
//                                _isLoading.value = false
//                                _appointments.value = null
//                                _errorMessage.value = error.localizedMessage ?: "Failed to refresh appointments"
//                            }
//                    } else {
//                        _appointments.value = null
//                        _errorMessage.value = "User token not available"
//
//                    }
//                }
//        }
//    }
//}
//
////package com.example.myapp.ui.viewModel
////
////import androidx.compose.runtime.collectAsState
////import androidx.lifecycle.LiveData
////import androidx.lifecycle.ViewModel
////import androidx.lifecycle.liveData
////import androidx.lifecycle.viewModelScope
////import com.example.myapp.data.model.AppointmentEntity
////import com.example.myapp.data.model.Property
////import com.example.myapp.data.repository.AppointmentRepository
////import com.example.myapp.data.repository.UserRepository
////import dagger.hilt.android.lifecycle.HiltViewModel
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.flow.MutableStateFlow
////import kotlinx.coroutines.flow.StateFlow
////import kotlinx.coroutines.flow.collectLatest
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.withContext
////import javax.inject.Inject
////
////@HiltViewModel
////class AppointmentViewModel @Inject constructor(
////    private val appointmentRepository: AppointmentRepository,
////    private val userRepository: UserRepository
////) : ViewModel() {
////    private val _appointments = MutableStateFlow<List<AppointmentEntity>>(emptyList())
////    val appointments : StateFlow<List<AppointmentEntity>?> = _appointments
////
////    private val _token = MutableStateFlow<String?>(null)
////    val token : StateFlow<String?> = _token
////
////    init {
//////        loadAppointments()
//////        tokenize()
////        refreshAppointments()
////    }
////
////    fun loadAppointments() {
////        viewModelScope.launch {
////            val res = appointmentRepository.getAppointments()
////            res.onSuccess { apps ->
////                _appointments.value = apps
////            }.onFailure {
////                println("SSomethings bad brah")
////            }
////
////        }
////    }
////
////    fun refreshAppointments(){
////        viewModelScope.launch {
////            userRepository.getUser().collectLatest { user ->
////                println(user)
////                (token as MutableStateFlow<String?>).value = user?.token
////                val res = appointmentRepository.refreshAppointments("Bearer ${token.value}")
////                res.onSuccess {
////                    loadAppointments()
////                }.onFailure {
////                    println("We coundn't get anything sadly? $res")
////                }
////            }
////        }
////    }
////
////    fun tokenize() {
////        viewModelScope.launch {
////            userRepository.getUser().collectLatest { user ->
////                println(user)
////                (token as MutableStateFlow<String?>).value = user?.token
////                refreshAppointments()
////            }
////
////        }
////    }
////}
