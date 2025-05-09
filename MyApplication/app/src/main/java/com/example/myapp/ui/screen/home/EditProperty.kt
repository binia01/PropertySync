package com.example.myapp.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.data.model.UserReqState
import com.example.myapp.ui.components.Header


@Composable
fun EditProperty(propertyId: String?,  navController: NavController){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header("Edit Property", true, { navController.popBackStack() })

        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(text = "Hello ${userState?.firstname}", style = MaterialTheme.typography.headlineSmall)
//
//        when (updateAccountState) {
//            is UserReqState.Loading -> CircularProgressIndicator()
//            is UserReqState.Error -> Text(
//                text = (updateAccountState as UserReqState.Error).message,
//                color = MaterialTheme.colorScheme.error
//            )
//            is UserReqState.Success -> Text(
//                text = (updateAccountState as UserReqState.Success).message.toString(),
//                color = MaterialTheme.colorScheme.primary
//            )
//            else -> {}
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        OutlinedTextField(
//            value = firstName,
//            onValueChange = { firstName = it },
//            label = { Text("First Name") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp)
//        )
//
//        OutlinedTextField(
//            value = lastName,
//            onValueChange = { lastName = it },
//            label = { Text("Last Name") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp)
//        )
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email address") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp)
//        )
//
//        Button(
//            onClick = {
//                userViewModel.update(
//                    first = firstName,
//                    last = lastName,
//                    newEmail = email,
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Submit Changes")
//        }
    }

}