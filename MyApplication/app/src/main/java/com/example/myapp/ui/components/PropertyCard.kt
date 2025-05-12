package com.example.myapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.data.model.Property
import com.example.myapp.R
import androidx.compose.material.icons.filled.LocationOn
import com.example.myapp.ui.theme.FailRed


@Composable
fun PropertyCard( property: Property, isSeller: Boolean, navController: NavController, onDelete: (() -> Unit)?=null ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                if (!isSeller) {
                    navController.navigate("propertyDetails/${property.id}")}
            },
        elevation = CardDefaults.cardElevation(defaultElevation=4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface))
    {
        Image(
            painter = painterResource(R.drawable.demohouse),
            contentDescription = "home",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(190.dp)
                .fillMaxSize()
        )
        Column (modifier = Modifier.padding(8.dp))
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(text=property.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f))
                Surface(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary
                ){
                    Text(
                        text = property.price.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = 4.dp))
                Text(text = property.location,
                    style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp))
            {
                IconText(R.drawable.outline_bed_24, "${property.beds} Beds")
                IconText(R.drawable.outline_bathtub_24, "${property.baths} Baths")
                IconText(R.drawable.outline_crop_square_24, "${property.area} sqft")
            }

                // Add seller buttons here
            if (isSeller) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("editProperty/${property.id}")
                        },
                        modifier = Modifier.padding(end = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Edit",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = { onDelete?.invoke() },
                        modifier = Modifier.padding(end = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = FailRed)
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }

        }

    }
}


@Preview
@Composable
fun PreviewPropCard(){
    val sampleProperty = Property(
        id = 1,
        title = "Modern Apartment",
        location = "New York, NY",
        price = 500,
        beds = 2,
        baths = 1,
        area = "850 sqft",
        imageUrl = "demohouse",
        description = "asdf",
        sellerId = 2, // For now, using a drawable name instead of URL
        status = "TEBEDA"
    )
//    PropertyCard(sampleProperty, true)
}

