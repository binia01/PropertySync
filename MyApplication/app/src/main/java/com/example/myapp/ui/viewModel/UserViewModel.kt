package com.example.myapp.ui.viewModel


import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Auth
import com.example.myapp.data.model.User
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {
    private val _userState = MutableStateFlow<User?>(null)
    val userState : StateFlow<User?> = _userState

    val userRole : StateFlow<String?> = MutableStateFlow<String?>(null)
    val userId : StateFlow<Int?> = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            userRepository.getUser().collect { loaduser ->
                println("USER FROM GET User IN USERVIEWMODEL = $loaduser")
                _userState.value = loaduser
                (userRole as MutableStateFlow).value = loaduser?.role
                (userId as MutableStateFlow).value = loaduser?.id
            }
        }
    }
    fun logout(){
        viewModelScope.launch {
            userRepository.deleteUser()
        }
    }

    fun update() {
        // TODO
//        viewModelScope.launch {
//            userRepository.
//        }
    }
}
