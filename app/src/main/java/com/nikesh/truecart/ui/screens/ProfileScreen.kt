package com.nikesh.truecart.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikesh.truecart.ui.viewmodel.ProfileViewModel
import androidx.navigation.NavController

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AsyncImage(
            model = "https://static.vecteezy.com/system/resources/thumbnails/010/260/479/small_2x/default-avatar-profile-icon-of-social-media-user-in-clipart-style-vector.jpg", // Placeholder
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "John Doe",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "john.doe@example.com",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                ListItem(
                    leadingContent = {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Orders")
                    },
                    headlineContent = { Text("Your Orders") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("orders") }
                )

                Divider()
                ListItem(
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = "Edit Profile") },
                    headlineContent = { Text("Edit Profile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("edit_profile") }
                )
                Divider()
                ListItem(
                    leadingContent = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    headlineContent = { Text("Settings") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("settings") }
                )
                Divider()
                ListItem(
                    leadingContent = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
                    headlineContent = { Text("Logout") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Handle logout */ }
                )
            }
        }
    }
}
