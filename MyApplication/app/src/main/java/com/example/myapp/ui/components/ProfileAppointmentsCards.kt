package com.example.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapp.R

@Composable
fun ProfileAppointmentsCards(
    pendingCount: Int,
    confirmedCount: Int,
    propertyCount: Int? = null,
    userRole: String
) {
    Column {
        AppointmentCard(
            title = "Pending Appointments",
            count = pendingCount,
            icon = painterResource(id = R.drawable.calander),
            linkText = "View all appointments",
            iconBgColor = Color(0xFF2979FF),
            onLinkClick = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppointmentCard(
            title = "Confirmed Appointments",
            count = confirmedCount,
            icon = painterResource(id = R.drawable.tick),
            linkText = "View confirmed appointments",
            iconBgColor = Color(0xFF00C853),
            onLinkClick = {}
        )
        if (userRole == "SELLER" && propertyCount != null) {
            Spacer(modifier = Modifier.height(12.dp))
            AppointmentCard(
                title = "My Properties",
                count = propertyCount,
                icon = painterResource(id = R.drawable.homeproperty),
                linkText = "View my properties",
                iconBgColor = Color(0xFF6366F1),
                onLinkClick = {}
            )
        }
    }
}

@Composable
fun AppointmentCard(
    title: String,
    count: Int,
    icon: Any,
    linkText: String,
    iconBgColor: Color,
    onLinkClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(iconBgColor, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (icon) {
                            is ImageVector -> Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            is androidx.compose.ui.graphics.painter.Painter -> Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            else -> throw IllegalArgumentException("Unsupported icon type")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(title, style = MaterialTheme.typography.labelLarge)
                        Text("$count", style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
            }
            Row(
                modifier = Modifier
                    .clickable{ onLinkClick() }
                    .fillMaxWidth()
                    .background(Color(0xFFF3F4F6))
                    .padding(horizontal = 16.dp, vertical = 20.dp)

            ) {
                Text(
                    text = linkText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2979FF)
                )
            }
        }
    }
}
