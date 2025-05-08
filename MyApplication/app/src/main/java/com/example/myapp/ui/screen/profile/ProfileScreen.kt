package com.example.myapp.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.UserInfoCard
import com.example.myapp.ui.components.ProfileAppointmentsCards
import com.example.myapp.ui.components.AccountSettings
import com.example.myapp.ui.components.BottomNav

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.myapp.ui.theme.CardBackground

@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
        topBar = { Header("My Profile") },
        bottomBar = { BottomNav(navController) },
        containerColor = Color(0xFFF9FAFB)
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            color = Color(0xFFF9FAFB)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = CardBackground),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 25.dp)

            ) {
                item {
                    UserInfoCard("Grid", "Grid@gmail.com", "Property Buyer")
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileAppointmentsCards(5, 2)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Account Settings",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    AccountSettings()
                }
            }
        }
    }
}

