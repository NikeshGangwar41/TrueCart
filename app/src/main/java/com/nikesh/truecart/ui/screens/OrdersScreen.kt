package com.nikesh.truecart.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikesh.truecart.ui.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(profileViewModel: ProfileViewModel) {
    val orders by profileViewModel.orders.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Orders") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (orders.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(orders) { order ->
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Text(text = "Order ID: ${order.id.take(8)}...", style = MaterialTheme.typography.titleSmall)
                            Text(text = "Total: $" + String.format("%.2f", order.totalAmount), style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(order.orderDate)}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            order.items.forEach { orderItem ->
                                Text(text = "- ${orderItem.product.title} x ${orderItem.quantity} @ $${String.format("%.2f", orderItem.priceAtOrder)}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Divider()
                    }
                }
            } else {
                Text(text = "No past orders", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
