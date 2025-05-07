package com.example.myapp.ui.screen.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapp.data.model.Auth
import com.example.myapp.ui.viewModel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        println("LoginScreen - AuthState: $authState")
        if (authState is Auth.LoggedIn) {
            println("LoginScreen - Login successful!")
        } else if (authState is Auth.Error) {
            println("LoginScreen - Login error: ${(authState as Auth.Error).message}")
        } else if (authState is Auth.Loading) {
            println("LoginScreen - AuthState: Loading...")
        } else if (authState is Auth.Idle) {
            println("LoginScreen - AuthState: Idle")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.padding(8.dp),
            visualTransformation = PasswordVisualTransformation() //hide
        )
        Button(
            onClick = { authViewModel.login(username, password) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }
        TextButton(onClick = { TODO() }) {
            Text("Sign Up")
        }
        if (authState is Auth.Error) {
            Text(
                text = (authState as Auth.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
        if (authState is Auth.Loading) {
            CircularProgressIndicator() // Show loading indicator
        }
    }
}
