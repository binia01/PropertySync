package com.example.myapp.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapp.data.model.Auth

import com.example.myapp.ui.viewModel.AuthViewModel


enum class UserType { BUYER, SELLER }
@Composable
fun SignUpScreen(onNavToLogin: () -> Unit, // navigate to sign up
                 authViewModel: AuthViewModel,
) {
    // State variables for form fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(UserType.BUYER) }

    // Error states
    var fullNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()


    // Main layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){// App Logo/Title
            Text(
                text = "PropertySync",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color(0xFF2196F3)
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Sign in title
            Text(
                text = "Sign in to your account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 24.dp))

        // Full name field
        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                fullNameError = ""
            },
            label = { Text("Full name") },
            isError = fullNameError.isNotEmpty(),
            supportingText = { if (fullNameError.isNotEmpty()) Text(fullNameError) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = ""
            },
            label = { Text("Email address") },
            isError = emailError.isNotEmpty(),
            supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = ""
            },
            label = { Text("Password") },
            isError = passwordError.isNotEmpty(),
            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Role selection title
        Text(
            text = "I am a:",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Buyer/Seller toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ChoiceChip(
                selected = userType == UserType.BUYER,
                onClick = { userType = UserType.BUYER },
                label = "Buyer"
            )
            ChoiceChip(
                selected = userType == UserType.SELLER,
                onClick = { userType = UserType.SELLER },
                label = "Seller"
            )
        }

        Button(
            onClick = {
                // Validate inputs
                var isValid = true

                if (fullName.isBlank()) {
                    fullNameError = "Full name is required"
                    isValid = false
                }

                if (email.isBlank()) {
                    emailError = "Email is required"
                    isValid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Enter a valid email"
                    isValid = false
                }

                if (password.isBlank()) {
                    passwordError = "Password is required"
                    isValid = false
                } else if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    isValid = false
                }

                if (isValid) {
                    authViewModel.singUp(
                        username = fullName,
                        email = email,
                        password = password,
                        role = userType.name
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) // Blue color
            )
        ) {
            if (authState is Auth.Loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Sign up")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account?")
            TextButton(onClick = onNavToLogin) {
                Text("Sign in")
            }
        }
    }
}

@Composable
fun ChoiceChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = if (selected) Color(0xFF2196F3) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        contentColor = when {
            selected -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            }
        ),
        tonalElevation = if (selected) 8.dp else 0.dp,
        shadowElevation = if (selected) 4.dp else 0.dp,
        onClick = onClick
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}

