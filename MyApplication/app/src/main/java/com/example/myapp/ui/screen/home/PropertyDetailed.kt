package com.example.myapp.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.HeaderStyle

@Composable
fun PropertyDetailed(propertyId: String?,  navController: NavController,){

    Column {
        Header(
            title = "Property Details",
            showBack = true,
            backgroundStyle = HeaderStyle.Blue
        )
        Text("Expecting the property ID here too ${propertyId?.toInt()}")
    }

}