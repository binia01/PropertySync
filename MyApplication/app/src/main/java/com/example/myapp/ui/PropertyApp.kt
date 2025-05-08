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
//import com.example.myapp.ui.screen.auth.LoginScreen
import com.example.myapp.ui.screen.auth.LoginScreenUI
import com.example.myapp.ui.screen.auth.SignUpScreen
import com.example.myapp.ui.screen.property.AddPropertyScreen
import com.example.myapp.ui.viewModel.AuthViewModel

@Composable

fun PropertyApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    // Redirect to login when user logs out
    LaunchedEffect(authState) {
        if (authState is Auth.LoggedOut) {
            navController.navigate(Screens.Login.route) {
                popUpTo(0) // clear stack
            }
        }
    }


    Scaffold(
        bottomBar = { if (authState is Auth.LoggedIn) BottomNav(navController = navController) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (authState is Auth.LoggedIn) Screens.Home.route else Screens.Login.route,
            Modifier.padding(paddingValues)
        ) {
            composable(Screens.Login.route) { LoginScreenUI(authViewModel, onSignInSuccess = { navController.navigate(Screens.Home.route) }, onNavigateToSignUp = {navController.navigate(Screens.SignUp.route)}) }
            composable(Screens.SignUp.route) { SignUpScreen(authViewModel, onSignUpSuccess = { navController.navigate(Screens.Home.route) }, onNavigateToSignIn = {navController.navigate(Screens.Login.route)}) }
            composable(Screens.Home.route) { HomeScreen(authViewModel = authViewModel,navController = navController) }
            composable(Screens.Bookings.route) { BookingScreen(navController = navController) }
            composable(Screens.Add.route) {AddPropertyScreen(navController = navController) }
        }
    }
}