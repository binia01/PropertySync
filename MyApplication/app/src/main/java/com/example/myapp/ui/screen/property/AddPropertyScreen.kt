package com.example.myapp.ui.screen.property

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.ui.components.BottomNav
import com.example.myapp.ui.components.Header

//import com.example.myapp.ui.components.Header

@Composable
fun AddPropertyScreen(navController: NavController){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var beds by remember { mutableStateOf(0) }
    var baths by remember { mutableStateOf(0) }
    var area by remember { mutableStateOf("") }
    Scaffold (
        containerColor = Color.White,
        topBar = { Header(title = "Create Property", showBack = true) },
        bottomBar = { BottomNav(navController = NavController(LocalContext.current)) },
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 40.dp, vertical = 40.dp),
        ){
            InputField(label = "Property Title *", hint = "e.g. Modern apartment for sale", value = title, num = 1, onValueChange = {title = it})
            InputField(label = "Price ($) *", hint = "e.g. 100000", value = price, num = 1,onValueChange = {price = it})
            InputField(label = "Location *", hint = "e.g. New york", value = location, num = 1,onValueChange = {location = it})
            InputField(label = "Area (sq ft) *", hint = "e.g. 2400", value = area, num = 1,onValueChange = {area = it})
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CounterField("Bedrooms *", beds, onIncrement = { beds++ }, onDecrement = { if (beds > 0) beds-- })
                CounterField("Bathrooms *", baths, onIncrement = { baths++ }, onDecrement = { if (baths > 0) baths-- })
            }

            Spacer(modifier = Modifier.height(16.dp))
            InputField(label = "Description *", hint = "Describe your property", value = description, num = 10, onValueChange = {description = it})
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { /* Cancel action */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    Text("Cancel", color = Color.Black)
                }

                TextButton(
                    onClick = { /* Create property */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .background(Color(0xFF2563EB), RoundedCornerShape(8.dp))
                ) {
                    Text("Create property", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, hint: String, value: String, num: Int, onValueChange: (String) -> Unit, KeyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default) {
    Text(label, style = MaterialTheme.typography.bodyMedium)
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint, style = MaterialTheme.typography.bodySmall) },
        keyboardOptions = KeyboardOptions,
        maxLines = num,
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
    )
    Spacer(Modifier.height(16.dp))

}



@Composable
fun CounterField(
    label: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                IconButton(onClick = onDecrement) {
                    Text(text = "−", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                IconButton(onClick = onIncrement) {
                    Text(text = "+", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PropertyScreenPreview(){
//    AddPropertyScreen()
}
