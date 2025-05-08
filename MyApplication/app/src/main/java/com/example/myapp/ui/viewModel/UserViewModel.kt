package com.example.myapp.ui.viewModel


import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.model.Auth
import com.example.myapp.data.model.User
import com.example.myapp.data.model.UserReqState
import com.example.myapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {
    private val _userState = MutableStateFlow<User?>(null)
    val userState : StateFlow<User?> = _userState

    val userRole : StateFlow<String?> = MutableStateFlow<String?>(null)
    val userId : StateFlow<Int?> = MutableStateFlow<Int?>(null)

    private val _deleteAccountState = MutableStateFlow<UserReqState>(UserReqState.Idle)
    val deleteAccountState: StateFlow<UserReqState> = _deleteAccountState.asStateFlow()

    private val _updateAccountState = MutableStateFlow<UserReqState>(UserReqState.Idle)
    val updateAccountState: StateFlow<UserReqState> = _updateAccountState.asStateFlow()


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
    fun deleteUser() {
        viewModelScope.launch {
            _deleteAccountState.value = UserReqState.Loading
            val result = userRepository.deleteUserAccount()
            result.onSuccess {
                _deleteAccountState.value = UserReqState.Success("Deleted!")
            }.onFailure {
                _deleteAccountState.value = UserReqState.Error(result.exceptionOrNull()?.message ?: "Failed to delete account")
            }
        }
    }



    fun update(first: String?, last: String? ,newEmail: String?) {
        println("THE NAMES ARE? $first $last $newEmail")
        viewModelScope.launch {
            val res = userRepository.updateUser(first?.takeIf { it.isNotBlank() }, newEmail?.takeIf { it.isNotBlank() }, last?.takeIf { it.isNotBlank() })
                res.onSuccess {
                    _updateAccountState.value = UserReqState.Success("Profile updated successfully")
                }
                .onFailure { error ->
                    _updateAccountState.value = UserReqState.Error(error.localizedMessage ?: "Failed to update profile")
                }
        }
    }


}
