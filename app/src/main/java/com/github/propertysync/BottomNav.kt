package com.github.propertysync

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.propertysync.ui.theme.LightGray
import com.github.propertysync.ui.theme.Primary

data class NavPage(val name: String, val iconResId: Int, val route: String)

object Routes{
    val HomePage = NavPage("Home", R.drawable.home, "home")
    val AppointmentsPage = NavPage("Appointments", R.drawable.appoint, "appointments")
    val AddPage = NavPage("Add", R.drawable.add, "add")
    val ProfilePage = NavPage("Profile", R.drawable.profile, "profile")

    val pages = listOf(HomePage, AppointmentsPage, AddPage, ProfilePage)
}

@Composable
fun NavBar(selectedRoute: String = Routes.HomePage.route, onChange: (String) -> Unit = {}){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
//            .padding(12.dp)
    ) {
        for (page in Routes.pages) {
            NavBarItem(
                modifier = Modifier
                    .clickable { onChange(page.route) } ,
                page,
                selected = selectedRoute == page.route
            )
        }
    }
}

@Preview
@Composable
fun NavBarItemPrev(){
    NavBarItem(modifier = Modifier,Routes.HomePage,true)
}


@Composable
fun NavBarItem(
    modifier: Modifier = Modifier,
    page: NavPage,
    selected: Boolean = false)
{
    val icon = ImageVector.vectorResource(id = page.iconResId)

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .padding(horizontal = 12.dp)
//            .background(if (selected) LightGray else null)
            .padding(12.dp)
    ) {
        Image(
            imageVector = icon,
            contentDescription = page.name,
            colorFilter = ColorFilter.tint(
                if (selected) Primary else Color.Black
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(24.dp)
        )
        Text(page.name,
            fontSize = 12.sp,
            color = if (selected) Primary else Color.Black
        )
    }
}