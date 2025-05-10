package com.example.myapp.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapp.R

@Composable
fun AccountSettings( onUpdateProfileClick: () -> Unit,
                     onDeleteAccountClick: () -> Unit,
                     onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        SettingsRow(
            "Update Profile", painterResource(id = R.drawable.editicon),
            onClick = { onUpdateProfileClick() },
            hasRightArrow = true
        )
        SettingsRow(
            "Delete Account",
            painterResource(id = R.drawable.trashicon),
            iconTint = Color.Red,
            textColor = Color.Red,
            onClick = { onDeleteAccountClick() }
        )
        SettingsRow("Logout",painterResource(id = R.drawable.logout), onClick = { onLogoutClick() })
    }
}

@Composable
fun SettingsRow(
    title: String,
    icon: Any,
    iconTint: Color = Color.Black,
    textColor: Color = Color.Black,
    onClick: () -> Unit,
    hasRightArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                is Painter -> Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                else -> throw IllegalArgumentException("Unsupported icon type")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = textColor)
        }
        if(hasRightArrow){
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
        }
    }
    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))
    Spacer(modifier = Modifier.height(8.dp))
}

