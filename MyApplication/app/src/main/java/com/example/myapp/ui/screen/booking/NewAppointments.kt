package com.example.myapp.ui.screen.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.HeaderStyle


@Composable
fun NewAppointments(navController: NavController){

    Column {
        Header(
            title = "Book a Property",
            showBack = true,
            onbackpressed = { navController.popBackStack() },
            backgroundStyle = HeaderStyle.Blue
        )

    }
}