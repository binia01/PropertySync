package com.example.myapp.ui.viewModel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Property
import com.example.myapp.data.repository.AppointmentRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _appointments = MutableStateFlow<List<AppointmentEntity>>(emptyList())
    val appointments : StateFlow<List<AppointmentEntity>?> = _appointments

    private val _token = MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token

    init {
//        loadAppointments()
//        tokenize()
        refreshAppointments()
    }

    fun loadAppointments() {
        viewModelScope.launch {
            val res = appointmentRepository.getAppointments()
            res.onSuccess { apps ->
                _appointments.value = apps
            }.onFailure {
                println("SSomethings bad brah")
            }

        }
    }

    fun refreshAppointments(){
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                println(user)
                (token as MutableStateFlow<String?>).value = user?.token
                val res = appointmentRepository.refreshAppointments("Bearer ${token.value}")
                res.onSuccess {
                    loadAppointments()
                }.onFailure {
                    println("We coundn't get anything sadly? $res")
                }
            }
        }
    }

    fun tokenize() {
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                println(user)
                (token as MutableStateFlow<String?>).value = user?.token
                refreshAppointments()
            }

        }
    }
}
