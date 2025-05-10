package com.example.myapp.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapp.ui.screen.property.AddPropertyState
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Request.AddPropertyRequest
import com.example.myapp.data.repository.AuthRepository
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import com.example.myapp.ui.screen.property.AddPropertyEvent
import com.example.myapp.ui.screen.property.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// AddPropertyViewModel.kt
@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state: StateFlow<AddPropertyState> = _state
    private val _token = MutableStateFlow<String>("")
    val token: StateFlow<String> = _token

    fun onEvent(event: AddPropertyEvent) {
        when (event) {
            is AddPropertyEvent.onTitleChange -> {
                _state.value = _state.value.copy(title = event.title)
            }
            is AddPropertyEvent.onPriceChange -> {
                _state.value = _state.value.copy(price = event.price)
            }
            is AddPropertyEvent.onLocationChange -> {
                _state.value = _state.value.copy(location = event.location)
            }
            is AddPropertyEvent.onBedsIncrease -> {
                _state.value = _state.value.copy(beds = event.beds + 1)
            }
            is AddPropertyEvent.onBedsDecrease -> {
                _state.value = _state.value.copy(beds = event.beds - 1)
            }
            is AddPropertyEvent.onBathsIncrease -> {
                _state.value = _state.value.copy(baths = event.baths + 1)
            }
            is AddPropertyEvent.onBathsDecrease -> {
                _state.value = _state.value.copy(baths = event.baths - 1)
            }
            is AddPropertyEvent.onAreaChange -> {
                _state.value = _state.value.copy(area = event.area)
            }
            is AddPropertyEvent.onDescriptionChange -> {
                _state.value = _state.value.copy(description = event.description)
            }
            is AddPropertyEvent.onClearClick -> {
                _state.value = AddPropertyState()
            }
            is AddPropertyEvent.onSubmitClick -> {
                if (validateForm()) {
                    loadUserDetails()
                }
            }
            is AddPropertyEvent.OnSuccessHandled -> {
                _state.value = _state.value.copy(isSuccessfull = false)
            }
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val req = try {
                AddPropertyRequest(
                    title = state.value.title,
                    price = state.value.price.toInt(),
                    description = state.value.description,
                    location = state.value.location,
                    area = state.value.area.toInt(),
                    baths = state.value.baths,
                    beds = state.value.beds
                )
            } catch (e: NumberFormatException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Invalid number format"
                )
                return@launch
            }

            when (val result = propertyRepository.createProperty(token.value, req)) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccessfull = true,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                if (user == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "User not found or token is null"
                    )
                } else {
                    (token as MutableStateFlow<String>).value = user.token
                    onSubmitClick()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        return when {
            state.value.title.isBlank() -> {
                _state.value = _state.value.copy(error = "Title is required")
                false
            }
            state.value.price.isBlank() || !state.value.price.matches(Regex("\\d+")) -> {
                _state.value = _state.value.copy(error = "Valid price is required")
                false
            }
            state.value.area.isBlank() || !state.value.area.matches(Regex("\\d+")) -> {
                _state.value = _state.value.copy(error = "Valid area is required")
                false
            }
            else -> true
        }
    }
}
