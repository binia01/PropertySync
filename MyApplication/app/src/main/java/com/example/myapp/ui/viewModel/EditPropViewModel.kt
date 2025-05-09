package com.example.myapp.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.api.PropertyService
import com.example.myapp.data.model.Auth
import com.example.myapp.data.model.User
import com.example.myapp.data.model.UserReqState
import com.example.myapp.data.repository.PropertyRepository
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPropViewModel @Inject constructor (
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token

    fun update(title: String?, description: String? , area: String?, beds: String?, baths:String?, location: String?, price: String?, id: String,) {
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                println(user)
                (token as MutableStateFlow<String?>).value = user?.token
                val res = propertyRepository.updateProperty(id, token = "Bearer ${token.value}", title?.takeIf { it.isNotBlank() }, description?.takeIf { it.isNotBlank() }, price?.takeIf { it.isNotBlank() }, beds?.takeIf { it.isNotBlank() }, baths?.takeIf { it.isNotBlank() }, location?.takeIf { it.isNotBlank() }, area?.takeIf { it.isNotBlank() })
                res.onSuccess {
                   println(res)
                }.onFailure {
                   println(res)
                }
            }
        }
    }

}
