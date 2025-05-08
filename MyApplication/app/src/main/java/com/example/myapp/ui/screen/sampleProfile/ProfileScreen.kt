package com.example.myapp.ui.screen.sampleProfile


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.UserReqState
import com.example.myapp.ui.navigation.Screens
import com.example.myapp.ui.viewModel.AuthViewModel
import com.example.myapp.ui.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()
    val deleteAccountState by userViewModel.deleteAccountState.collectAsState()
    val updateAccountState by userViewModel.updateAccountState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Account Actions", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Hello ${userState?.firstname}", style = MaterialTheme.typography.headlineLarge)


        if (deleteAccountState is UserReqState.Loading || updateAccountState is UserReqState.Loading) {
            CircularProgressIndicator()
        } else if (deleteAccountState is UserReqState.Error || updateAccountState is UserReqState.Error) {
            val errorMessage = (deleteAccountState as? UserReqState.Error)?.message
                ?: (updateAccountState as? UserReqState.Error)?.message
            Text(
                text = errorMessage.toString(),
                color = MaterialTheme.colorScheme.error
            )
        } else if (updateAccountState is UserReqState.Success) {
            Text(
                text = (updateAccountState as UserReqState.Success).message.toString(),
                color = MaterialTheme.colorScheme.primary
            )
        } else if (deleteAccountState is UserReqState.Success) {
            LaunchedEffect(Unit) {
                navController.navigate(Screens.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.logOut()
                navController.navigate(Screens.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        // delete user button
        Button(
            onClick = {
                userViewModel.deleteUser()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Delete Account")
        }

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
            },
            label = { Text("Full name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // update button make same on yours please and thank you.
        Button(
            onClick = {
                userViewModel.update(
                    name = fullName,
                    newEmail = email,
                )
            },
            modifier = Modifier.fillMaxWidth() // Make the button fill width for consistency
        ) {
            Text("Submit Changes")
        }
    }
}