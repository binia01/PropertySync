package com.example.myapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myapp.R
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.HeaderStyle
import com.example.myapp.ui.components.IconText
import com.example.myapp.ui.theme.BluePrimary
import com.example.myapp.ui.viewModel.DetailedPropertyViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetails(navController: NavHostController, propertyId: String?) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("MM/DD/YYYY")}
    var selectedTime by remember { mutableStateOf(" 00 : 00") }
    var showTimePicker by remember { mutableStateOf(false) }

    val detailedPropertyViewModel: DetailedPropertyViewModel = hiltViewModel()
    val property by detailedPropertyViewModel.selectedProperty.collectAsState()

    detailedPropertyViewModel.getPropertyDetails(propertyId?.toIntOrNull())

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            title = "Property Details",
            showBack = true,
            onbackpressed = { navController.popBackStack() },
            backgroundStyle = HeaderStyle.Blue
        )
        Image(
            painter = painterResource(id = R.drawable.demohouse),
            contentDescription = "Property Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Property Name
                Text(
                    text = "${property?.title}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f))

                // Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text="${property?.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp))
                Text(
                    text = "${property?.description}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp))

                Spacer(Modifier.height(16.dp))

                // Property Features
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp)) {
                    IconText(R.drawable.outline_bed_24, "${property?.beds} Beds")
                    IconText(R.drawable.outline_bathtub_24, "${property?.baths} Baths")
                    IconText(R.drawable.outline_crop_square_24, "${property?.area} sqft")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "${property?.price}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${property?.status}",
                        color = Color(0xFF166534),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .background(Color(0xFFDCFCE7), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                HorizontalDivider(thickness = 1.dp, color = Color.LightGray,
                    modifier = Modifier
                        .padding(bottom = 16.dp))
                Text(
                    text = "Schedule Appointment for ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                //To Pick date
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painterResource(R.drawable.calander),
                        contentDescription = "Date",
                        colorFilter = ColorFilter.tint(Color.Black))
                    Spacer(Modifier.width(4.dp))

                    TextButton(onClick = { showDatePicker = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        ))
                    { Text("Date: $selectedDate ")}
                }
                //To pick time
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.clock),
                        contentDescription = "Time",
                        modifier = Modifier.size(30.dp))
                    Spacer(Modifier.width(4.dp))
                    TextButton(onClick = { showTimePicker = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        ))
                    { Text("At: $selectedTime ")}
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)


                // Schedule Button
                Button(
                    onClick = {
                        val propertyIdInt = propertyId?.toIntOrNull()
                        if (propertyIdInt != null) {
                            detailedPropertyViewModel.createAppointment(propertyIdInt, selectedDate, selectedTime, property?.sellerId)
                            println("Clicked Schedule and called ViewModel: $selectedDate, $selectedTime, $propertyId")
                        } else {
                            println("Error: propertyId is null or not an integer")
                            // Optionally show an error message to the user
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary
                    )
                ) {
                    Text("Schedule", color = Color.White)
                }


                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = {
                            selectedDate = it?.let { millis -> convertMillisToDatee(millis) } ?: ""
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }

                if (showTimePicker) {
                    TimePickerPopup(
                        onDismiss = { showTimePicker = false },
                        onTimeSelected = { hour, minute ->
                            selectedTime = String.format("%02d:%02d", hour, minute)
                        }
                    )
                }
            }
        }
    }
    }

// Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState() // For ui to remember

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) { Text("OK") }},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") }}
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerPopup(
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val now = remember { Calendar.getInstance() }
    val timeState = rememberTimePickerState(
        initialHour = now.get(Calendar.HOUR_OF_DAY),
        initialMinute = now.get(Calendar.MINUTE),
        is24Hour = false
    )

    val containerColor = Color(0xFFF5F5F5)
    val inputColor = Color(0xFF90CAF9) // Light blue
    val buttonColor = BluePrimary

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Smaller and blue-themed time input
                TimeInput(
                    state = timeState,
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .height(56.dp),
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = inputColor.copy(alpha = 0.3f),
                        timeSelectorUnselectedContainerColor = inputColor.copy(alpha = 0.3f),
                        timeSelectorSelectedContentColor = Color.Black,
                        timeSelectorUnselectedContentColor = Color.Black,
                        periodSelectorSelectedContainerColor = inputColor.copy(alpha = 0.3f),
                        selectorColor = inputColor.copy(alpha = 0.3f),
                        containerColor = inputColor.copy(alpha = 0.3f),
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = buttonColor)
                    }
                    Button(
                        onClick = {
                            onTimeSelected(timeState.hour, timeState.minute)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            }
        }
    }
}

fun convertMillisToDatee(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

