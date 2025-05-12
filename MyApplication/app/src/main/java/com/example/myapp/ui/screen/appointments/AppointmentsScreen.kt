package com.example.myapp.ui.screen.appointments


import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.R
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Property
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.HeaderStyle
import com.example.myapp.ui.screen.home.DatePickerModal
import com.example.myapp.ui.theme.BluePrimary
import com.example.myapp.ui.viewModel.AppointmentViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsPage(navController: NavController) {
    val tabs = listOf("All", "Pending", "Confirmed", "Completed", "Cancelled")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val appointmentViewModel: AppointmentViewModel = hiltViewModel()
    val appointments by appointmentViewModel.appointments.collectAsState()

    val userRole by appointmentViewModel.userRole.collectAsState()


    Column {
            Header(
                title = "My Appointments",
                showBack = false,
                backgroundStyle = HeaderStyle.Blue
            )
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = BluePrimary,
                edgePadding = 0.dp,
                divider = {},
                indicator = {},
                containerColor = Color.Transparent,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        interactionSource = remember { MutableInteractionSource() },
                        modifier = if (selectedTabIndex == index) {
                            Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                .height(36.dp)
                                .background(
                                    color = BluePrimary.copy(0.2f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        } else {
                            Modifier
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                .height(36.dp)
                        },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                color = if (selectedTabIndex == index) BluePrimary else Color.Black,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Medium else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        },
                        selectedContentColor = Color.Transparent,
                        unselectedContentColor = Color.Transparent,
                        enabled = true
                    )
                }
            }
                appointments?.let {
                    if (it.isEmpty()) {
                        NoAppointments()
                    } else {
                        LazyColumn(modifier = Modifier.padding(16.dp, vertical = 4.dp)) {
                            itemsIndexed(it) { index, appointment: AppointmentEntity ->
                                var propertyDetails by remember { mutableStateOf<Property?>(null) }

                                LaunchedEffect(appointment.propid) {
                                    println("AppointmentsPage: Fetching property details for propId: ${appointment.propid}")
                                    appointmentViewModel.getPropertyDetails(appointment.propid).collectLatest { propertyResult ->
                                        println("AppointmentsPage: Received property result for propId: ${appointment.propid}, Result: $propertyResult")
                                        propertyDetails = propertyResult.getOrNull()
                                        println("AppointmentsPage: propertyDetails updated: $propertyDetails")
                                    }
                                }
                                AppointmentCard(
                                    title = propertyDetails?.title ?: "Couldn't get title",
                                    date = appointment.date,
                                    time = appointment.startTime,
                                    address = propertyDetails?.location ?: "Couldn't get location",
                                    status = appointment.status,
                                    onEditBuyer ={ newDate, newTime->  appointmentViewModel.updateAppointment(
                                        appointment.id,
                                        date = newDate,
                                        startTime = newTime,
                                    )},
                                    onCancel = { appointmentViewModel.updateAppointmentStatus(appointment.id, "CANCELED") },
                                    onConfirm = { appointmentViewModel.updateAppointmentStatus(appointment.id, "CONFIRMED") },
                                    isSeller = userRole == "SELLER"
                                )
                            }
                        }
                    }
                }
        }
    }




@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateString(dateString: String, formatter: DateTimeFormatter): String {
    return try {
        LocalDateTime.parse(dateString, formatter).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } catch (e: DateTimeParseException) {
        "Invalid Date"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentCard(
    title: String,
    date: String,
    time: String,
    address: String,
    status: String = "Pending",
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onEditBuyer: (newDate: String, newTime: String) -> Unit = { _, _ -> },
    isSeller: Boolean = false
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val formattedDate = try {
        LocalDateTime.parse(date, formatter).format(dateFormatter)
    } catch (e: DateTimeParseException) {
        "Invalid Date"
    }
    val formattedTime = try {
        LocalDateTime.parse(time, formatter).format(timeFormatter)
    } catch (e: DateTimeParseException) {
        "Invalid Time"
    }

    val statusColor = when (status) {
        "CONFIRMED" -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
        "PENDING" -> Pair(Color(0xFFFEF9C3), Color(0xFF854D0E))
        "CANCELLED" -> Pair(Color(0xFFFDD8D8), Color(0xFFB71C1C))
        "COMPLETED" -> Pair(Color(0xFFC8E6C9), Color(0xFF1B5E20))
        else -> Pair(Color.LightGray, Color.DarkGray)
    }

    var isEditMode by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(formattedDate) }
    var selectedTime by remember { mutableStateOf(formattedTime) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDatePicker) {
        val year = selectedDate.split("-")[0].toIntOrNull() ?: LocalDate.now().year
        val month = (selectedDate.split("-")[1].toIntOrNull() ?: LocalDate.now().monthValue) - 1
        val day = selectedDate.split("-")[2].toIntOrNull() ?: LocalDate.now().dayOfMonth

        DatePickerModal(
            onDateSelected = {
                selectedDate = it?.let { millis -> convertMillisToDatee(millis) } ?: ""
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        val hour = selectedTime.split(":")[0].toIntOrNull() ?: LocalTime.now().hour
        val minute = selectedTime.split(":")[1].toIntOrNull() ?: LocalTime.now().minute
        TimePickerDialog(
            context,
            { _, hourOfDay, minuteOfHour ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                showTimePicker = false
            },
            hour,
            minute,
            true
        ).show()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = status,
                    color = statusColor.second,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(statusColor.first, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isEditMode && !isSeller && status == "PENDING") {
                //To Pick date
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.calander),
                        contentDescription = "Date",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                    Spacer(Modifier.width(4.dp))

                    TextButton (
                        onClick = { showDatePicker = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        )
                    )
                    { Text("Date: $selectedDate ") }
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
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    TextButton(
                        onClick = { showTimePicker = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        )
                    )
                    { Text("At: $selectedTime ") }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        onEditBuyer(
                            selectedDate,
                            selectedTime
                        )
                        isEditMode = false
                    }) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isEditMode = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(id = R.drawable.clock), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = formattedTime,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = address,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (status == "PENDING") {
                        if (isSeller) {
                            Button(
                                onClick = onConfirm,
                                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Confirm", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onCancel,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Cancel", color = Color.White)
                            }
                        } else { // It's a buyer
                            Button(
                                onClick = { isEditMode = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9A825)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onCancel,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    } else if (isSeller && status == "CONFIRMED") {
                        // TODO change from button to just a confirmed green box?
                        Button(
                            onClick = { /* TODO: Implement "Mark as Completed" */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Complete", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoAppointments() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.calander),
            contentDescription = "Calendar Icon",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "No appointments found",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You don't have any appointments yet.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun convertMillisToDatee(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}


