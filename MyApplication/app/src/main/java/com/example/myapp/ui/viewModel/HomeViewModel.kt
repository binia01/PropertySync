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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties : StateFlow<List<Property>?> = _properties

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole : StateFlow<String?> = _userRole

    private val _userId  = MutableStateFlow<Int?>(null)
    val userId : StateFlow<Int?> = _userId

    private val _token = MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token

    init {
        loadUserDetails()
    }

    private fun loadProperties(){
        viewModelScope.launch {
            print("HomeView Properties $token")
            if(userRole.value == "BUYER"){
                val res = propertyRepository.getProperties("Bearer ${token.value}")
                res.onSuccess { props ->
                    println("HomeView it seems we got the propertiess $props")
                    _properties.value = props
                }.onFailure { error ->
                    println("PropertyGet for Buyer failed in viewmodel: ${error.localizedMessage}")
                }
            }else{
                println("Getting the properties good sar")
                propertyRepository.getLocalProps().collectLatest{ p ->
                    _properties.value = p
                }
            }
        }
    }

    private fun loadUserDetails(){
        viewModelScope.launch {
            userRepository.getUser().collectLatest { user ->
                (userRole as MutableStateFlow<String?>).value = user?.role
                (userId as MutableStateFlow<Int?>).value = user?.id
                (token as MutableStateFlow<String?>).value = user?.token

                loadProperties()
            }
        }
    }

    fun deleteProperty(propId: Int){
        viewModelScope.launch {
            propertyRepository.deleteProperty(propId, "Bearer ${token.value}")
        }
    }
}
