package com.example.myapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.R

enum class HeaderStyle {
    Secondary,
    Blue
}

@Composable
fun Header(
    title: String,
    showBack: Boolean = false,
    onbackpressed: (() -> Unit)? = null,
    subtitle: String? = null,
    backgroundStyle: HeaderStyle = HeaderStyle.Secondary
) {
    val backgroundColor = when (backgroundStyle) {
        HeaderStyle.Secondary -> MaterialTheme.colorScheme.surface
        HeaderStyle.Blue -> Color(0xFF2563EB)
    }

    val textColor = if (backgroundStyle == HeaderStyle.Blue)
        Color.White
    else
        MaterialTheme.colorScheme.onBackground

    Surface(
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showBack) {
                    IconButton(
                        onClick = { onbackpressed?.invoke() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )
                    subtitle?.let {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HeaderPrevDark() {
    Header(
        title = "Appointments",
        subtitle = "No upcoming events",
        showBack = true,
        backgroundStyle = HeaderStyle.Secondary
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderPrev() {
    Header(
        title = "Appointments",
        subtitle = "No upcoming events",
        showBack = true,
        backgroundStyle = HeaderStyle.Secondary
    )
}
