package com.example.myapp.ui.screen.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.UserReqState
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.viewModel.UserViewModel
import com.example.myapp.R
import com.example.myapp.ui.theme.TextPrimary
import com.example.myapp.ui.theme.TextSecondary

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
    var isPrefilled by remember { mutableStateOf(false) }

    LaunchedEffect(userState) {
        userState?.let { user ->
            if (!isPrefilled) {
                firstName = user.firstname
                lastName = user.lastname
                email = user.email
                isPrefilled = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()){
        Header(
            title = "Update Profile",
            showBack = true,
            onbackpressed = { navController.popBackStack() }
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE6EFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF007BFF)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

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

                    Spacer(modifier = Modifier.height(24.dp))

                    DefaultInputField("First Name", firstName) { firstName = it }
                    Spacer(modifier = Modifier.height(12.dp))
                    DefaultInputField("Last Name", lastName) { lastName = it }
                    Spacer(modifier = Modifier.height(12.dp))
                    DefaultInputField("Email", email) { email = it }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            userViewModel.update(
                                first = firstName,
                                last = lastName,
                                newEmail = email,
                            )
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .width(160.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Changes", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text("Enter $label", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)) },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF007BFF),
                cursorColor = Color(0xFF007BFF)
            )
        )
    }
}