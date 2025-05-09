package com.example.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.BluePrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(title: String, showBack: Boolean = false, onbackpressed: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BluePrimary) // Blue background
            .padding(horizontal = 16.dp, vertical = 12.dp), // Add some padding
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBack) {
            IconButton(
                onClick = { onbackpressed?.invoke() }, // Handle back press
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary // White icon
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary), // White text
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun HeaderPrev() {
    Header("abs",showBack = true);
}