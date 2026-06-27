package com.example.webdev2

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- DATA CLASSES ---
data class Branch(
    val name: String,
    val imageRes: Int,
    val address: String
)

// HELPER: Bouncy Click Animation
@Composable
fun Modifier.bounceClick(): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale"
    )

    return this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { }
        )
}

@Composable
fun BranchSelectionScreen(onBranchSelected: (String) -> Unit) {
    val branches = listOf(
        Branch("Sm Lucena City Jolibee", R.drawable.sm_lucena, "SM City Lucena, Maharlika Highway"),
        Branch("Jollibee Lucena Bayan", R.drawable.lucena_bayan, "Quezon Avenue, Lucena City")
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BgGray)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Pink, PinkLight)))
                .padding(vertical = 40.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome to Jollibee!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Select a branch near you", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(branches) { branch ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .bounceClick()
                        .clickable { onBranchSelected(branch.name) }
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = branch.imageRes),
                            contentDescription = branch.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = Pink, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(branch.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                            }
                            Text(branch.address, fontSize = 13.sp, color = TextGray)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(
    cartItems: MutableList<FoodItem>,
    selectedBranch: String,
    onBackClick: () -> Unit,
    onItemAdded: (String) -> Unit
) {
    val foods = listOf(
        FoodItem("Yum Burger", 120, "Burger", "Juicy beef patty...", R.drawable.yumburger),
        FoodItem("Small Fries", 80, "Fries", "Crispy golden potato...", R.drawable.smallfries),
        FoodItem("Spaghetti with Drink", 90, "Meal", "Sweet spaghetti...", R.drawable.spagdrink),
        FoodItem("Coke Float", 180, "Drink", "Ice-cold cola float...", R.drawable.cokefloat),
        FoodItem("1pc Chicken with Rice", 150, "Meal", "Crispy chicken joy...", R.drawable.chimkenrice),
        FoodItem("2pc Burgersteak", 170, "Meal", "Two beef patties...", R.drawable.burgersteak),
        FoodItem("8pc Chicken Joy Bucket", 699, "Meal", "8pcs of Joy!", R.drawable.chickenbucket)
    )

    var searchQuery by remember { mutableStateOf("") }
    val filtered = foods.filter { searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BgGray)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Pink, PinkLight)))
            .padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Ordering from: $selectedBranch", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text("Welcome to Jollibee", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = onBackClick, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) {
                    Text("Change", fontSize = 11.sp)
                }
            }
        }

        OutlinedTextField(
            value = searchQuery, onValueChange = { searchQuery = it },
            placeholder = { Text("Search food...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = CardWhite, focusedContainerColor = CardWhite)
        )

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered, key = { it.name }) { food ->
                Box(Modifier.animateItem()) {
                    FoodCard(food) {
                        cartItems.add(food.copy())
                        onItemAdded(food.name)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodItem, onAddClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = food.imageRes), null, modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(food.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("₱${food.price}", color = Pink, fontWeight = FontWeight.ExtraBold)
            }
            Button(onClick = onAddClick, colors = ButtonDefaults.buttonColors(containerColor = Pink)) {
                Text("Add")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartItems: MutableList<FoodItem>) {
    val total = cartItems.sumOf { it.price }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BgGray)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Pink, PinkLight)))
            .padding(20.dp)) {
            Text("Your Cart  🛒", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", fontWeight = FontWeight.Bold, color = TextGray)
            }
        } else {
            LazyColumn(modifier = Modifier
                .weight(1f)
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(items = cartItems, key = { index, item -> "${item.name}_$index" }) { index, item ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { if (it == SwipeToDismissBoxValue.EndToStart) { cartItems.removeAt(index); true } else false }
                    )
                    Box(Modifier.animateItem()) {
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                Box(Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFCC0000)), contentAlignment = Alignment.CenterEnd) {
                                    Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.padding(end = 24.dp))
                                }
                            }
                        ) {
                            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardWhite), shape = RoundedCornerShape(16.dp)) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painterResource(id = item.imageRes), null, Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(item.name, fontWeight = FontWeight.Bold)
                                        Text("₱${item.price}", color = Pink)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total: ₱$total", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Pink)
                    Button(onClick = { showDialog = true }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = Pink)) {
                        Text("Place Order")
                    }
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = { Button(onClick = { showDialog = false }) { Text("OK") } }, title = { Text("Success!") }, text = { Text("Order Placed!") })
    }
}

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BgGray)) {

        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Pink, PinkLight)))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(ChipSel), contentAlignment = Alignment.Center) {
                    Text("👤", fontSize = 48.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("John Allen A. Seares", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
                Text("Lucena City, Philippines", color = Color.White.copy(alpha = 0.8f))
            }
        }

        // Made-up History Section
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Recent Order",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            // This is the hardcoded "Made-up" Past Order Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("#JLB-102938", fontWeight = FontWeight.Bold, color = Pink)
                        Text("2 days ago", fontSize = 12.sp, color = TextGray)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1x 8pc Chicken Joy Bucket, 2x Spaghetti",
                        fontSize = 14.sp,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(color = BgGray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sm Lucena City", fontSize = 12.sp, color = TextGray)
                        Text("₱879.00", fontWeight = FontWeight.ExtraBold, color = TextDark)
                    }
                }
            }
        }
    }
}