package com.nikesh.truecart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nikesh.truecart.ui.theme.TrueCartTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikesh.truecart.data.api.RetrofitClient
import com.nikesh.truecart.data.repository.ProductRepository
import com.nikesh.truecart.ui.screens.HomeScreen
import com.nikesh.truecart.ui.screens.ProductDetailScreen
import com.nikesh.truecart.ui.viewmodel.ProductListViewModelFactory
import com.nikesh.truecart.ui.viewmodel.ProductListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikesh.truecart.ui.viewmodel.ProductDetailViewModelFactory
import com.nikesh.truecart.ui.viewmodel.CartViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nikesh.truecart.ui.screens.CartScreen
import com.nikesh.truecart.ui.screens.ProfileScreen
import com.nikesh.truecart.ui.viewmodel.ProductDetailViewModel
import com.nikesh.truecart.ui.screens.WishlistScreen
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import com.nikesh.truecart.ui.viewmodel.WishlistViewModel
import com.nikesh.truecart.ui.viewmodel.ProfileViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.nikesh.truecart.ui.screens.OrdersScreen
import com.nikesh.truecart.ui.screens.EditProfileScreen
import com.nikesh.truecart.ui.screens.SettingsScreen

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Cart : Screen("cart", Icons.Default.ShoppingCart, "Cart")
    object Wishlist : Screen("wishlist", Icons.Default.Favorite, "Wishlist")
    object Profile : Screen("profile", Icons.Default.Person, "Profile")
    object ProductDetail : Screen("product_detail/{productId}", Icons.Default.Home, "ProductDetail") // Icon not used for detail screen
    object Orders : Screen("orders", Icons.Default.ShoppingCart, "Orders") // Icon not used for detail screen
    object EditProfile : Screen("edit_profile", Icons.Default.Edit, "Edit Profile")
    object Settings : Screen("settings", Icons.Default.Settings, "Settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrueCartTheme {
                val navController = rememberNavController()
                val productRepository = ProductRepository(RetrofitClient.apiService)
                val cartViewModel: CartViewModel = viewModel()
                val wishlistViewModel: WishlistViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()

                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    cartViewModel.newOrder.collect { newOrder ->
                        profileViewModel.addOrder(newOrder)
                    }
                }

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            val productListViewModel: ProductListViewModel = viewModel(
                                factory = ProductListViewModelFactory(productRepository)
                            )
                            HomeScreen(productListViewModel, navController, wishlistViewModel)
                        }
                        composable(Screen.ProductDetail.route) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toInt()
                            productId?.let {
                                val productDetailViewModel: ProductDetailViewModel = viewModel(
                                    factory = ProductDetailViewModelFactory(productRepository, it)
                                )
                                ProductDetailScreen(productDetailViewModel, cartViewModel, wishlistViewModel)
                            }
                        }
                        composable(Screen.Cart.route) {
                            CartScreen(cartViewModel)
                        }
                        composable(Screen.Wishlist.route) {
                            WishlistScreen(wishlistViewModel, cartViewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(profileViewModel, navController)
                        }
                        composable(Screen.Orders.route) {
                            OrdersScreen(profileViewModel)
                        }
                        composable(Screen.EditProfile.route) {
                            EditProfileScreen()
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Home,
        Screen.Cart,
        Screen.Wishlist,
        Screen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}