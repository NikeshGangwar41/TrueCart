package com.nikesh.truecart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nikesh.truecart.data.model.WishlistItem
import com.nikesh.truecart.ui.viewmodel.WishlistViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(wishlistViewModel: WishlistViewModel = viewModel(), cartViewModel: CartViewModel = viewModel()) {
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Wishlist") })
        }
    ) { paddingValues ->
        if (wishlistItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Your wishlist is empty.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                items(wishlistItems) { wishlistItem ->
                    WishlistItemView(
                        wishlistItem = wishlistItem,
                        onRemoveWishlist = { wishlistViewModel.removeProductFromWishlist(wishlistItem.product) },
                        onAddToCart = {
                            cartViewModel.addProductToCart(wishlistItem.product)
                            wishlistViewModel.removeProductFromWishlist(wishlistItem.product)
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistItemView(wishlistItem: WishlistItem, onRemoveWishlist: () -> Unit, onAddToCart: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = wishlistItem.product.image,
                contentDescription = wishlistItem.product.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = wishlistItem.product.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "$" + wishlistItem.product.price, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onAddToCart) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Move to Cart")
                }
                IconButton(onClick = onRemoveWishlist) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove from Wishlist")
                }
            }
        }
    }
}
