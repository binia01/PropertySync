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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapp.ui.navigation.Screens
import com.example.myapp.ui.theme.BaseBackground
import com.example.myapp.ui.theme.CardBackground
import com.example.myapp.ui.viewModel.AuthViewModel
import com.example.myapp.ui.viewModel.UserViewModel

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userViewModel: UserViewModel = hiltViewModel()
    val userState by userViewModel.userState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()){
        Header("My Profile")
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    UserInfoCard(
                        "${userState?.firstname} ${userState?.lastname}",
                        "${userState?.email}",
                        "Property ${userState?.role?.lowercase()}"
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfileAppointmentsCards(5, 2)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Account Settings",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    AccountSettings(
                        onUpdateProfileClick = { navController.navigate(Screens.UpdateProfile.route) },
                        onDeleteAccountClick = { userViewModel.deleteUser() },
                        onLogoutClick = {
                            println("did i get clicked?")
                            authViewModel.logOut()
                            navController.navigate(Screens.Login.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        },
                    )
                }
            }
        }
    }
}