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
) { val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()
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

        Button(
            onClick = {
                userViewModel.deleteUser()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Delete Account")
        }

        // Observe deleteAccountState for loading and error messages
        val deleteAccountState by userViewModel.deleteAccountState.collectAsState()
        if (deleteAccountState is UserReqState.Loading) {
            CircularProgressIndicator()
        } else if (deleteAccountState is UserReqState.Error) {
            Text(
                text = (deleteAccountState as UserReqState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        } else if (deleteAccountState is UserReqState.Success) {
            LaunchedEffect(Unit) {
                navController.navigate(Screens.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }
}
