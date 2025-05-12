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
    val tabs = listOf("All", "Pending", "Confirmed", "Completed", "Cancelled")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val appointmentViewModel: AppointmentViewModel = hiltViewModel()
    val appointments by appointmentViewModel.appointments.collectAsState()

    val userRole by appointmentViewModel.userRole.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Appointments",
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                windowInsets = WindowInsets(0.dp)// To remove more padding of system (finally)
            )
        }
    ){paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                        Text(text = "No appointments Yet.")
                            Button(
                                modifier = Modifier.padding(vertical = 24.dp),
                                onClick = { navController.navigate(Screens.Home.route)}
                            ) {
                                Text("Check Deals",
                                    color = Color.White)
                            }

                        }
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
}
