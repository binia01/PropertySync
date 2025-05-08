package com.example.myapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.myapp.data.model.Auth
import com.example.myapp.ui.components.BottomNav
import com.example.myapp.ui.screen.booking.BookingScreen
import com.example.myapp.ui.screen.home.HomeScreen
import com.example.myapp.ui.navigation.Screens
import com.example.myapp.ui.screen.auth.LoginScreen
import com.example.myapp.ui.viewModel.AuthViewModel

@Composable

fun PropertyApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()


    Scaffold(
        bottomBar = { if (authState is Auth.LoggedIn) BottomNav(navController = navController) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (authState is Auth.LoggedIn) Screens.Home.route else Screens.Login.route,
            Modifier.padding(paddingValues)
        ) {
            composable(Screens.Login.route) { LoginScreen(authViewModel) }
            composable(Screens.Home.route) { HomeScreen(navController = navController) }
            composable(Screens.Bookings.route) { BookingScreen(navController = navController) }


        }
    }
}