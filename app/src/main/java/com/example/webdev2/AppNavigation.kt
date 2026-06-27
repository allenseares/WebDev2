package com.example.webdev2

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// Color Constants
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
    val description: String = "",
    val imageRes: Int
)

sealed class Screen(val route: String) {
    object Food : Screen("food")
    object Profile : Screen("profile")
    object Cart : Screen("cart")
}

@Composable
fun AppNavigation() {
    var selectedBranchName by remember { mutableStateOf("") }
    var hasSelectedBranch by remember { mutableStateOf(false) }
    val cartItems = remember { mutableStateListOf<FoodItem>() }

    AnimatedContent(
        targetState = hasSelectedBranch,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.9f) togetherWith
                    fadeOut(animationSpec = tween(500))
        },
        label = "BranchTransition"
    ) { targetSelected ->
        if (!targetSelected) {
            BranchSelectionScreen(onBranchSelected = { branch: String ->
                selectedBranchName = branch
                hasSelectedBranch = true
            })
        } else {
            MainAppContent(
                cartItems = cartItems,
                selectedBranch = selectedBranchName,
                onBackToBranch = { hasSelectedBranch = false }
            )
        }
    }
}

@Composable
fun MainAppContent(
    cartItems: MutableList<FoodItem>,
    selectedBranch: String,
    onBackToBranch: () -> Unit
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Logic for the Add to Cart Popup (Snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = BgGray,
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Register Snackbar
        bottomBar = {
            NavigationBar(containerColor = CardWhite, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = currentRoute == Screen.Food.route,
                    onClick = { navController.navigate(Screen.Food.route) },
                    label = { Text("Food", fontSize = 11.sp) },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Cart.route,
                    onClick = { navController.navigate(Screen.Cart.route) },
                    label = { Text("Cart", fontSize = 11.sp) },
                    icon = {
                        BadgedBox(badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge(containerColor = Pink) { Text(cartItems.size.toString()) }
                            }
                        }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Profile.route,
                    onClick = { navController.navigate(Screen.Profile.route) },
                    label = { Text("Profile", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Food.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Food.route) {
                FoodScreen(
                    cartItems = cartItems,
                    selectedBranch = selectedBranch,
                    onBackClick = onBackToBranch,
                    onItemAdded = { itemName ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = "$itemName added to cart!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Cart.route) { CartScreen(cartItems) }
        }
    }
}