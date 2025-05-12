package com.example.myapp.ui.screen.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.AppointmentEntity
import com.example.myapp.ui.viewModel.AppointmentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController){
    val appointmentViewModel: AppointmentViewModel = hiltViewModel()
    val appointments by appointmentViewModel.appointments.collectAsState()


    Column {
        appointments?.let {
            if (it.isEmpty()) {
                // Display a message if there are no appointments
                Text(text = "No appointments available.")
            } else {
                // Display the list of appointments
                LazyColumn {
                    itemsIndexed(it) { index, appointment: AppointmentEntity -> // Explicitly type 'appointment'
                        OldAppointmentCard(appointment = appointment)
                    }
                }
            }
        }
    }

}