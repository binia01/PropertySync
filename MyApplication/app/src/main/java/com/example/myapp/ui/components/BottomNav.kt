package com.example.myapp.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import com.example.myapp.data.model.NavItems
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapp.ui.viewModel.UserViewModel


@Composable
fun BottomNav(navController: NavController) {
    var navSelectedItem by remember {mutableIntStateOf(0)}
    val userViewModel: UserViewModel = viewModel()
    val userRole by userViewModel.userRole.collectAsState()
    val user by userViewModel.userState.collectAsState()

    println("BOTTOM NAV USER ROLE BEFORE GOING INT IF USER ROLE != NULL = $userRole")


    if (userRole != null){
        println("HIIIII FUCKERRR THE USER ROLE IS AFTER GOING INSIDE NOT NULL (BOTTOM NAV) $userRole, $user")
        NavigationBar {
            NavItems.bottomNavigationItems(userRole!!).forEachIndexed { i, navItem ->
                NavigationBarItem(
                    selected = i == navSelectedItem,
                    label = {
                        Text(navItem.label,
                            modifier = Modifier.weight(1f))},
                    icon = { Icon(
                            navItem.icon,
                            contentDescription = navItem.label)},
                    onClick = {
                        navSelectedItem = i
                        navController.navigate(navItem.route){
                            popUpTo(navController.graph.startDestinationId){
                                saveState = true}
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}