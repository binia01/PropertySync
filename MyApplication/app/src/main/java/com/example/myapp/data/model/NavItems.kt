package com.example.myapp.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapp.ui.navigation.Screens

data class NavItems (
    val label: String = "",
    val icon: ImageVector = Icons.Outlined.Home,
    val route: String = "",
) {
    companion object {
        fun bottomNavigationItems(userRole: String): List<com.example.myapp.data.model.NavItems> {
            var defaultNav = mutableListOf<com.example.myapp.data.model.NavItems>(
                NavItems(
                    label = "Home",
                    icon = Icons.Outlined.Home,
                    route = Screens.Home.route
                ),
                NavItems(
                    label = "Appointments",
                    icon = Icons.Outlined.DateRange,
                    route = Screens.Bookings.route
                )
            )
            if (userRole == "SELLER") {
                defaultNav.add(
                    NavItems(
                        label = "Add Property",
                        icon = Icons.Default.Add,
                        route = Screens.Add.route
                    )
                )
            }
            defaultNav.add(
                NavItems(
                    label = "Profile",
                    icon = Icons.Outlined.Person,
                    route = Screens.Profile.route
                )
            )
            return defaultNav
        }
    }
}
