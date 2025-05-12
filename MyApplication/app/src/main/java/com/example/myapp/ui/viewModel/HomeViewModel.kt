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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _properties = MutableStateFlow<List<Property>?>(null)
    val properties: StateFlow<List<Property>?> = _properties

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<MessageState>(MessageState.Idle)
    val message: StateFlow<MessageState> = _errorMessage

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    init {
        loadUserDetails()
        observePropertyUpdates()
    }
    private fun loadProperties() {
        viewModelScope.launch {
            userRepository.getUser()
                .collectLatest { user ->
                    val authToken = user?.token
                    val currentUserId = user?.id

                    if (!authToken.isNullOrBlank()) {
                        _isLoading.value = true
                        propertyRepository.getProperties("Bearer $authToken").collectLatest { result ->
                            _isLoading.value = false
                            if (result.isSuccess) {
                                _properties.value = result.getOrNull()
                                val filteredProperties = if (userRole.value == "SELLER" && currentUserId != null) {
                                    _properties.value?.filter { it.sellerId == currentUserId } // Corrected comparison
                                } else {
                                    _properties.value
                                }
                                _properties.value = filteredProperties
                            } else {
                                _properties.value = null
                                _errorMessage.value = MessageState.Error(result.exceptionOrNull()?.localizedMessage ?: "Failed to fetch properties")
                            }
                        }
                    } else {
                        _properties.value = null
                        _errorMessage.value = MessageState.Error("User token not available")
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
    private fun observePropertyUpdates() {
        viewModelScope.launch {
            propertyRepository.propertyUpdatedFlow.collectLatest {
                println("HomeViewModel: propertyUpdatedFlow emitted, refreshing properties...")
                loadProperties()
            }
        }
    }
    fun deleteProperty(propId: Int) {
        println("Can I DELETE THIS PROPERTY??? $propId")
        viewModelScope.launch {
            userRepository.getUser().map { it?.token }.collectLatest { authToken ->
                if (!authToken.isNullOrBlank()) {
                    propertyRepository.deleteProperty(propId, "Bearer $authToken")
                        .onFailure { error ->
                            _errorMessage.value = MessageState.Error( error.localizedMessage ?: "Failed to delete property")
                        }
                        .onSuccess {
                            // Optionally handle successful deletion (e.g., refresh list)
                            _errorMessage.value = MessageState.Success("Deleted Property")
                            loadProperties()
                        }
                } else {
                    _errorMessage.value = MessageState.Error( "Cannot delete property, user token not available")
                }
            }
        }
    }
}

sealed class MessageState {
    object Idle: MessageState()
    object Loading : MessageState()
    data class Success(val message: String) : MessageState()
    data class Error(val message: String) : MessageState()

}