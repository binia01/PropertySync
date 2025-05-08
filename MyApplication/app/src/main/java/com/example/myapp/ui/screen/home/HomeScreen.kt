package com.example.myapp.ui.screen.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.Property
import com.example.myapp.ui.components.Header
import com.example.myapp.ui.components.PropertyCard
import com.example.myapp.ui.navigation.Screens
import com.example.myapp.ui.viewModel.AuthViewModel
import com.example.myapp.ui.viewModel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val userRole by homeViewModel.userRole.collectAsState()
    val userId by homeViewModel.userId.collectAsState()
    val properties by homeViewModel.properties.collectAsState()

    val propertiesList = properties ?: emptyList()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Header(
            title = "Find Your Dream Home",
        )
        if (userRole == "SELLER"){
            LazyColumn {
                itemsIndexed(propertiesList){ index, it -> PropertyCard(it, true)}
            }
        }else{
            LazyColumn {
                itemsIndexed(propertiesList) { index, it -> PropertyCard(it, false)}
            }
        }


    }
}