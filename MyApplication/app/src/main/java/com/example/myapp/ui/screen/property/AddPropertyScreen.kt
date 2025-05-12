package com.example.myapp.ui.screen.property

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.viewModel.AddPropertyViewModel
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun AddPropertyScreen(
    navController: NavController,
    viewModel: AddPropertyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Handle success state
    LaunchedEffect(state.isSuccessfull) {
        if (state.isSuccessfull) {
            // Reset the form and navigate to home
            navController.navigate("home") {
                popUpTo("add_property") { inclusive = true }
            }
            viewModel.onEvent(AddPropertyEvent.OnSuccessHandled)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header(title = "Create Property", showBack = false)

        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {
                InputField(
                    label = "Property Title *",
                    hint = "e.g. Modern apartment for sale",
                    value = state.title,
                    num = 1,
                    onValueChange = { viewModel.onEvent(AddPropertyEvent.onTitleChange(it)) }
                )

                InputField(
                    label = "Price ($) *",
                    hint = "e.g. 100000",
                    value = state.price,
                    num = 1,
                    onValueChange = { viewModel.onEvent(AddPropertyEvent.onPriceChange(it)) }
                )

                InputField(
                    label = "Location *",
                    hint = "e.g. New york",
                    value = state.location,
                    num = 1,
                    onValueChange = { viewModel.onEvent(AddPropertyEvent.onLocationChange(it)) }
                )

                InputField(
                    label = "Area (sq ft) *",
                    hint = "e.g. 2400",
                    value = state.area,
                    num = 1,
                    onValueChange = { viewModel.onEvent(AddPropertyEvent.onAreaChange(it)) }
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CounterField(
                        "Bedrooms *",
                        state.beds,
                        onIncrement = { viewModel.onEvent(AddPropertyEvent.onBedsIncrease(state.beds)) },
                        onDecrement = {
                            if (state.beds > 0) viewModel.onEvent(AddPropertyEvent.onBedsDecrease(state.beds))
                        }
                    )
                    CounterField(
                        "Bathrooms *",
                        state.baths,
                        onIncrement = { viewModel.onEvent(AddPropertyEvent.onBathsIncrease(state.baths)) },
                        onDecrement = { if (state.baths > 0) viewModel.onEvent(AddPropertyEvent.onBathsDecrease(state.baths)) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = "Description *",
                    hint = "Describe your property",
                    value = state.description,
                    num = 10,
                    onValueChange = { viewModel.onEvent(AddPropertyEvent.onDescriptionChange(it))}
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { viewModel.onEvent(AddPropertyEvent.onClearClick)},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .border(
                                1.dp,
                                Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    TextButton(
                        onClick = { viewModel.onEvent(AddPropertyEvent.onSubmitClick) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .background(
                                Color(0xFF2563EB),
                                RoundedCornerShape(8.dp)
                            ),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text("Create property", color = Color.White)
                        }
                    }
                }

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    hint: String,
    value: String,
    num: Int,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            maxLines = num
        )
    }
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Start)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Decrement button
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(topStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 8.dp)
                    )
                    .clickable { onDecrement() },
                contentAlignment = Alignment.Center
            ) {
                Text("−", style = MaterialTheme.typography.bodyLarge)
            }

            // Centered counter
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    count.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Increment button
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 0.dp)
                    )
                    .clickable { onIncrement() },
                contentAlignment = Alignment.Center
            ) {
                Text("+", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}