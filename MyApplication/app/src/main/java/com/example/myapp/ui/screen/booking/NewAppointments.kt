package com.example.myapp.ui.screen.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapp.ui.components.Header


@Composable
fun NewAppointments(navController: NavController){

    Column {
        Header(
            "Book a Property", true,
            onbackpressed = { navController.popBackStack() }
        )

    }
}