package com.example.myapp.ui.screen.profile


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.UserReqState
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.viewModel.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfile(
    navController: NavController,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()
    val updateAccountState by userViewModel.updateAccountState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            title = "Update Profile",
            showBack = true,
            onbackpressed = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Hello ${userState?.firstname}", style = MaterialTheme.typography.headlineSmall)

        when (updateAccountState) {
            is UserReqState.Loading -> CircularProgressIndicator()
            is UserReqState.Error -> Text(
                text = (updateAccountState as UserReqState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            is UserReqState.Success -> Text(
                text = (updateAccountState as UserReqState.Success).message.toString(),
                color = MaterialTheme.colorScheme.primary
            )
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                userViewModel.update(
                   first = firstName,
                    last = lastName,
                    newEmail = email,
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Changes")
        }
    }
}
