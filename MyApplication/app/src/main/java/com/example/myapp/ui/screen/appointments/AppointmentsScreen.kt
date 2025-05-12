package com.example.myapp.ui.screen.appointments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.data.model.Property
import com.example.myapp.ui.components.AppointmentCard
import com.example.myapp.ui.navigation.Screens
import com.example.myapp.ui.theme.BluePrimary
import com.example.myapp.ui.viewModel.AppointmentViewModel
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsPage(navController: NavController) {
    val tabs = listOf("ALL", "PENDING", "CONFIRMED", "COMPLETED", "CANCELLED")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val appointmentViewModel: AppointmentViewModel = hiltViewModel()
    val appointments by appointmentViewModel.appointments.collectAsState()
    val userRole by appointmentViewModel.userRole.collectAsState()

    val filteredAppointments = remember(appointments, selectedTabIndex) {
        appointments?.filter { appointment ->
            when (selectedTabIndex) {
                0 -> true // ALL
                1 -> appointment.status == "PENDING"
                2 -> appointment.status == "CONFIRMED"
                3 -> appointment.status == "COMPLETED"
                4 -> appointment.status == "CANCELLED"
                else -> true
            }
        } ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Appointments",
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                windowInsets = WindowInsets(0.dp)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Simplified tab row
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) BluePrimary else Color.Black
                            )
                        }
                    )
                }
            }

            // Appointment list
            if (filteredAppointments.isEmpty()) {
                EmptyAppointmentsView(navController)
            } else {
                AppointmentList(
                    appointments = filteredAppointments,
                    appointmentViewModel = appointmentViewModel,
                    userRole = userRole.toString()
                )
            }
        }
    }
}

@Composable
private fun EmptyAppointmentsView(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No appointments yet.")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { navController.navigate(Screens.Home.route) }
        ) {
            Text("Check Deals", color = Color.White)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppointmentList(
    appointments: List<AppointmentEntity>,
    appointmentViewModel: AppointmentViewModel,
    userRole: String
) {
    LazyColumn(modifier = Modifier.padding(16.dp, vertical = 4.dp)) {
        itemsIndexed(appointments) { index, appointment ->
            var propertyDetails by remember { mutableStateOf<Property?>(null) }

            LaunchedEffect(appointment.propid) {
                appointmentViewModel.getPropertyDetails(appointment.propid).collectLatest {
                    propertyDetails = it.getOrNull()
                }
            }

            AppointmentCard(
                title = propertyDetails?.title ?: "Loading...",
                date = appointment.date,
                time = appointment.startTime,
                address = propertyDetails?.location ?: "Loading...",
                status = appointment.status,
                onEditBuyer = { newDate, newTime ->
                    appointmentViewModel.updateAppointment(
                        appointment.id,
                        date = newDate,
                        startTime = newTime
                    )
                },
                onCancel = {
                    appointmentViewModel.updateAppointmentStatus(
                        appointment.id,
                        "CANCELLED"
                    )
                },
                onConfirm = {
                    appointmentViewModel.updateAppointmentStatus(
                        appointment.id,
                        "CONFIRMED") },
                isSeller = userRole == "SELLER") }
    }
}