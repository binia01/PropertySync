package com.example.myapp.ui.screen.home


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.components.PropertyCard
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

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Find Your Dream Home",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                windowInsets = WindowInsets(0.dp)
            ) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize())
        {
            if (userRole == "SELLER"){
                    itemsIndexed(propertiesList){ index, it -> PropertyCard(it, true, navController,
                        onDelete = { homeViewModel.deleteProperty(it.id.toInt())})}

            }else{
                    itemsIndexed(propertiesList) { index, it -> PropertyCard(it, false, navController)}
            }
        }
    }
}