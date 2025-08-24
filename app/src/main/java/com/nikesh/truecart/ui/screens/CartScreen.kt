package com.nikesh.truecart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikesh.truecart.ui.viewmodel.CartViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalCartPrice = cartViewModel.getTotalCartPrice()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Cart") })
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total: $" + String.format("%.2f", totalCartPrice),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                cartViewModel.checkout()
                            }
                        }
                    ) {
                        Text("Checkout")
                    }
                }
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(cartItems) { cartItem ->
                    CartItemView(
                        cartItem = cartItem,
                        onAddProduct = { cartViewModel.addProductToCart(cartItem.product) },
                        onRemoveProduct = { cartViewModel.removeProductFromCart(cartItem.product) }
                    )
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemView(
    cartItem: com.nikesh.truecart.data.model.CartItem,
    onAddProduct: () -> Unit,
    onRemoveProduct: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.product.image,
                contentDescription = cartItem.product.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$" + cartItem.product.price,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Quantity: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemoveProduct) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove One")
                }
                Text(
                    "${cartItem.quantity}",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onAddProduct) {
                    Icon(Icons.Default.Add, contentDescription = "Add One")
                }
            }
        }
    }
}
