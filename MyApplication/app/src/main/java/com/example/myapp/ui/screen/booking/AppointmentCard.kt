package com.example.myapp.ui.screen.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.data.model.AppointmentEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OldAppointmentCard(appointment: AppointmentEntity) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formattedStartTime = formatDateString(appointment.startTime, formatter)


    println(appointment)

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            Text(text = "Appointment ID: ${appointment.id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Status: ${appointment.status}")
            Text(text = "Start Time: $formattedStartTime")
            Text(text = "Property ID: ${appointment.propid}")
//            Text(text = "Role: ${appointment.role}")
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