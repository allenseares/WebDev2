package com.example.webdev2

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

val Pink = Color(0xFF750000)
val PinkLight = Color(0xFFB60000)
val BgGray = Color(0xFFF6F6F6)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A1A1A)
val TextGray = Color(0xFF888888)
val ChipSel = Color(0xFFFFE4E4)

data class FoodItem(
    val name: String,
    val price: Int,
    val category: String,
    val description: String = ""
)

sealed class Screen(val route: String) {
    object Food : Screen("food")
    object Profile : Screen("profile")
    object Cart : Screen("cart")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cartItems = remember { mutableStateListOf<FoodItem>() }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = BgGray,
        bottomBar = {
            NavigationBar(
                containerColor = CardWhite,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentRoute == Screen.Food.route,
                    onClick = { navController.navigate(Screen.Food.route) },
                    label = { Text("Food", fontSize = 11.sp) },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Food") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Pink,
                        selectedTextColor = Pink,
                        indicatorColor = ChipSel,
                        unselectedIconColor = TextGray,
                        unselectedTextColor = TextGray
                    )
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.Cart.route,
                    onClick = { navController.navigate(Screen.Cart.route) },
                    label = { Text("Cart", fontSize = 11.sp) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(containerColor = Pink) {
                                        Text(
                                            cartItems.size.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Pink,
                        selectedTextColor = Pink,
                        indicatorColor = ChipSel,
                        unselectedIconColor = TextGray,
                        unselectedTextColor = TextGray
                    )
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.Profile.route,
                    onClick = { navController.navigate(Screen.Profile.route) },
                    label = { Text("Profile", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Pink,
                        selectedTextColor = Pink,
                        indicatorColor = ChipSel,
                        unselectedIconColor = TextGray,
                        unselectedTextColor = TextGray
                    )
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Food.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Food.route) { FoodScreen(cartItems) }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Cart.route) { CartScreen(cartItems) }
        }
    }
}