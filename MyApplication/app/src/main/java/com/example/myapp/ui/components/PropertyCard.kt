package com.example.myapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
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
import com.example.myapp.data.model.Property
import com.example.myapp.R
import androidx.compose.material.icons.filled.LocationOn



@Composable
fun PropertyCard( property: Property, isSeller: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation=4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)


        ){
        Image(
            painter = painterResource(R.drawable.demohouse),
            contentDescription = "home",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(190.dp)
                .fillMaxSize()
        )
        Column (modifier = Modifier
            .padding(8.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(text=property.title, style = MaterialTheme.typography.titleMedium)
                Surface(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color(37, 99,235)
                ){
                    Text(
                        text = property.price.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = property.location, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_bed_24),
                        contentDescription = "Beds",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text="${property.beds} Beds", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_bathtub_24),
                        contentDescription = "bath",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "${property.baths} Baths", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_crop_square_24),
                        contentDescription = "area",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "${property.area} ", style = MaterialTheme.typography.bodySmall)
                }

                // Add seller buttons here
            }
            if (isSeller){
                //TODO
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(end = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(37, 99,235))
                    ) {
                        Text(
                            text = "Edit",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(end = 8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(220,38,38))
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
        sellerId = 2 // For now, using a drawable name instead of URL
    )
    PropertyCard(sampleProperty, true)
}

