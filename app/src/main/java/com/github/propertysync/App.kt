package com.github.propertysync

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.propertysync.pages.AddPage
import com.github.propertysync.pages.AppointmentsPage
import com.github.propertysync.pages.HomePage
import com.github.propertysync.pages.ProfilePage

@Preview
@Composable
fun App() {
    var selectedRoute = remember { mutableStateOf(Routes.HomePage.route) }

    Scaffold(
        topBar = {
        AppTitle()
    }, bottomBar = {
        //State hoisting
            NavBar(
                selectedRoute=selectedRoute.value,
                onChange = {
                    selectedRoute.value = it
                })
        }
        
    ) {
        Box(modifier = Modifier.padding(it)) {

            when(selectedRoute.value){
                Routes.HomePage.route -> HomePage()
                Routes.AppointmentsPage.route -> AppointmentsPage()
                Routes.AddPage.route -> AddPage()
                Routes.ProfilePage.route -> ProfilePage()
            }
        }
    }
}


@Preview
@Composable
private fun AppTitle() {
    
}