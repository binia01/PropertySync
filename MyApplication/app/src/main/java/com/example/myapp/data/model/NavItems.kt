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
    val rout: String = "",
) {
    companion object {
        fun bottomNavigationItems(userRole: String): List<com.example.myapp.data.model.NavItems> {
            var defualtNav = mutableListOf<com.example.myapp.data.model.NavItems>(
                NavItems(
                    label = "Home",
                    icon = Icons.Outlined.Home,
                    rout = Screens.Home.route
                ),
                NavItems(
                    label = "Bookings",
                    icon = Icons.Outlined.DateRange,
                    rout = Screens.Bookings.route
                )
            )
            if (userRole == "SELLER") {
                defualtNav.add(
                    NavItems(
                        label = "Add Property",
                        icon = Icons.Default.Add,
                        rout = Screens.Add.route
                    )
                )
            }
            defualtNav.add(
                NavItems(
                    label = "Profile",
                    icon = Icons.Outlined.Person,
                    rout = Screens.Profile.route
                )
            )
            return defualtNav
        }
    }
}
