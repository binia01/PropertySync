package com.example.myapp.ui.navigation

sealed class Screens(val route : String) {
    object Home : Screens("home")
    object Bookings : Screens("booking")
    object Profile : Screens("profile")
    object Add: Screens("add_prop")
    object Login: Screens("login")
    object SignUp : Screens("signup")
    object UpdateProfile: Screens("updateForm")
    object PropertyDetails: Screens("propertyDetail")
    object Appointment: Screens("appointment")
}