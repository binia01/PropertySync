package com.example.myapp.ui.components



import com.example.myapp.ui.theme.CardBackground
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.data.model.Property
import com.example.myapp.R




@Composable
fun PropertyCard( property: Property, isSeller: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation=4.dp),


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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(text=property.title, style = MaterialTheme.typography.titleMedium)
                Text(text = property.price.toString(), style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = property.location, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Text(text="${property.beds} Beds", style = MaterialTheme.typography.bodySmall)
                Text(text = "${property.baths} Baths", style = MaterialTheme.typography.bodySmall)
                Text(text = "${property.area} ", style = MaterialTheme.typography.bodySmall)
                // Add seller buttons here
            }
            if (isSeller){
                //TODO
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Edit")
                    Text("Delete")
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
    PropertyCard(sampleProperty, false)
}

