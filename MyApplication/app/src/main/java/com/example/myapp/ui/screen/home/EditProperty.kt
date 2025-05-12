package com.example.myapp.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.HeaderStyle
import com.example.myapp.ui.viewModel.EditPropViewModel
import com.example.myapp.ui.viewModel.EditState


@Composable
fun EditProperty(propertyId: String?,  navController: NavController){
    val editPropViewModel: EditPropViewModel = hiltViewModel()
    // TODO
    val editState by editPropViewModel.editState.collectAsState()
    val scrollState = rememberScrollState()


    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var baths by remember { mutableStateOf("") }
    var beds by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            title = "Edit Property",
            showBack = true,
            onbackpressed = { navController.popBackStack() },
            backgroundStyle = HeaderStyle.Blue
        )
        Spacer(modifier = Modifier.height(24.dp))

        // TODO
        when (editState) {
            is EditState.Loading -> CircularProgressIndicator()
            is EditState.Error -> Text(
                text = (editState as EditState.Error).message,
                color = MaterialTheme.colorScheme.error
            )

            is EditState.Success -> Text(
                text = (editState as EditState.Success).message.toString(),
                color = MaterialTheme.colorScheme.primary
            )

            else -> {}
        }
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = baths,
                onValueChange = { baths = it },
                label = { Text("Baths") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = beds,
                onValueChange = { beds = it },
                label = { Text("Beds") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = area,
            onValueChange = { area = it },
            label = { Text("Area (sq ft)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            editPropViewModel.update(
                title = title,
                description = description,
                area = area,
                beds = beds,
                baths = baths,
                location = location,
                price = price,
                id = propertyId.toString(),
            )
        }) {
            Text("Save Changes")
        }
    }
}