package com.nikesh.truecart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikesh.truecart.ui.viewmodel.ProductDetailViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import com.nikesh.truecart.ui.viewmodel.CartViewModel
import com.nikesh.truecart.ui.viewmodel.WishlistViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun ProductDetailScreen(viewModel: ProductDetailViewModel, cartViewModel: CartViewModel, wishlistViewModel: WishlistViewModel = viewModel()) {
    val product by viewModel.product.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val wishlistItems by wishlistViewModel.wishlistItems.collectAsState()

    val isWishlisted = product?.let { p -> wishlistItems.any { it.product.id == p.id } } ?: false

    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (error != null) {
            Text(
                text = "Error: $error",
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (product != null) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    AsyncImage(
                        model = product!!.image,
                        contentDescription = product!!.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    IconButton(
                        onClick = {
                            if (isWishlisted) {
                                wishlistViewModel.removeProductFromWishlist(product!!)
                            } else {
                                wishlistViewModel.addProductToWishlist(product!!)
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = product!!.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Category: ${product!!.category}", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Rating: ${product!!.rating.rate} (${product!!.rating.count})", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$" + product!!.price, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product!!.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { cartViewModel.addProductToCart(product!!) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Add to Cart")
                }
            }
        } else {
            Text("Product not found", modifier = Modifier.align(Alignment.Center))
        }
    }
}
