package com.example.myapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Property
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _properties = MutableStateFlow<List<Property>?>(null)
    val properties: StateFlow<List<Property>?> = _properties

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    init {
        loadUserDetails()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            userRepository.getUser()
                .map { user -> user?.token }
                .collectLatest { authToken ->
                    if (!authToken.isNullOrBlank()) {
                        _isLoading.value = true
                        propertyRepository.getProperties("Bearer $authToken").collectLatest { result ->
                            _isLoading.value = false
                            if (result.isSuccess) {
                                _properties.value = result.getOrNull()
                                _errorMessage.value = null
                            } else {
                                _properties.value = null
                                _errorMessage.value = result.exceptionOrNull()?.localizedMessage ?: "Failed to fetch properties"
                            }
                        }
                    } else {
                        _properties.value = null
                        _errorMessage.value = "User token not available"
                    }
                }
        }
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                (userRole as MutableStateFlow<String?>).value = user?.role
                (userId as MutableStateFlow<Int?>).value = user?.id
                (token as MutableStateFlow<String?>).value = user?.token

                // Trigger property loading after user details (including token) are available
                loadProperties()
            }
        }
    }

    fun deleteProperty(propId: Int) {
        viewModelScope.launch {
            userRepository.getUser().map { it?.token }.collectLatest { authToken ->
                if (!authToken.isNullOrBlank()) {
                    propertyRepository.deleteProperty(propId, "Bearer $authToken")
                        .onFailure { error ->
                            _errorMessage.value = error.localizedMessage ?: "Failed to delete property"
                        }
                        .onSuccess {
                            // Optionally handle successful deletion (e.g., refresh list)
                            loadProperties()
                        }
                } else {
                    _errorMessage.value = "Cannot delete property, user token not available"
                }
            }
        }
    }
}