package com.example.myapp.ui.screen.appointments


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapp.R
import com.example.myapp.ui.theme.BluePrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsPage(navController: NavHostController,onBackPressed: () -> Unit) {
    val tabs = listOf("All", "Pending", "Confirmed", "Completed", "Cancelled")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Appointments",
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp))
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed()}) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back arrow",
                            modifier = Modifier
                                .requiredSize(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary
                ),
                windowInsets = WindowInsets(0.dp)// To remove more padding of system (finally)
            )
        }
    ) { paddingValues ->
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

            LazyColumn(modifier = Modifier
                .padding(16.dp, vertical = 4.dp)){
                items(sampleAppointments) { appointment ->
                    AppointmentCard(
                        title = appointment.title,
                        date = appointment.date,
                        time = appointment.time,
                        address = appointment.address,
                        status = appointment.status,
                        onCancel = {}
                    )
                }
            }
        }
    }
}

data class Appointment(
    val title: String,
    val date: String,
    val time: String,
    val address: String,
    val status: String // "Pending", "Confirmed", etc.
)


val sampleAppointments = listOf(
    Appointment(
        title = "Modern Apartment with View",
        date = "2025-05-09",
        time = "10:30 AM",
        address = "221B Baker Street, London",
        status = "Confirmed"
    ),
    Appointment(
        title = "Downtown Loft Visit",
        date = "2025-05-10",
        time = "02:00 PM",
        address = "455 Sunset Blvd, Los Angeles",
        status = "Pending"
    ),
    Appointment(
        title = "Beach House Tour",
        date = "2025-05-11",
        time = "11:15 AM",
        address = "123 Ocean Drive, Miami",
        status = "Confirmed"
    )
)


@Composable
fun AppointmentCard(
    title: String ,
    date: String ,
    time: String ,
    address: String,
    status: String = "Pending",
    onCancel: () -> Unit = {}
) {
    val statusColor = when (status) {
        "Confirmed" -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
        "Pending" -> Pair(Color(0xFFFEF9C3), Color(0xFF854D0E))
        else -> Pair(Color.LightGray, Color.DarkGray)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)) {

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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = date,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(id = R.drawable.clock), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = time,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(text = address,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        }
    }
}
