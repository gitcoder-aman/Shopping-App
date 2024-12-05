package com.tech.indiaekartshoppinguser.presentation.screens.wishlist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.seemore.components.EachProductView
import com.tech.indiaekartshoppinguser.presentation.screens.seemore.components.SearchBarView
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun WishListScreen(
    navController: NavHostController,
    shoppingViewmodel: ShoppingViewmodel
) {
    LaunchedEffect(Unit) {
        shoppingViewmodel.getAllFavouriteProducts()
    }
    val getAllFavouriteProducts =
        shoppingViewmodel.getAllFavouriteProducts.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var searchTextValue by remember {
        mutableStateOf("")
    }

    if (getAllFavouriteProducts.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MainColor)
        }
    }
    else if (getAllFavouriteProducts.value.productsData.isNotEmpty()) {

        val favList = if(searchTextValue.isNotEmpty()){
            getAllFavouriteProducts.value.productsData.filter {
                it.productName.contains(searchTextValue, ignoreCase = true)
            }
        }else{
            getAllFavouriteProducts.value.productsData
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteColor)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(), verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(top = 100.dp, start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = BlackColor,
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigateUp()
                            }
                    )
                    Text(
                        text = "See Your Favourite one",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.Start,
                            color = BlackColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ),
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    SearchBarView(
                        value = searchTextValue,
                        onValueChange = {
                            searchTextValue = it
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = DarkGrayColor
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favList) {
                            EachProductView(
                                productModel = it,
                                onClick = {
                                    navController.navigate(
                                        Routes.ProductDetailScreen(
                                            productId = it.productId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ellipse_1),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd)
            )

        }
    }
    else if(getAllFavouriteProducts.value.error.isNotEmpty()) {
        Toast.makeText(context, getAllFavouriteProducts.value.error, Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
fun SmoothGradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust height as needed
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White, // Top color
                        MainColor, // Smooth transition to light gray
                        MainColor  // Bottom color
                    ),
                    startY = 0f,
                    endY = 1000f // Adjust gradient spread
                )
            )
    ) {
        // Your content here
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = "One Shoulder Linen Dress",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(4) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700) // Gold color for stars
                    )
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Color.Gray // Half star or empty star
                )
            }

            // Price and Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rs. 5740",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "See more",
                    color = Color.Blue,
                    modifier = Modifier.clickable { /* Handle click */ }
                )
            }

            // Size Selector
            Text(text = "Size", fontWeight = FontWeight.Bold, color = Color.Black)
            Row {
                listOf("UK 8", "UK 10", "UK 12").forEach { size ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable { /* Handle size selection */ }
                    ) {
                        Text(text = size, color = Color.Black)
                    }
                }
            }

            // Quantity Selector
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Color", fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { /* Handle decrement */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Decrease")
                    }
                    Text(text = "1", modifier = Modifier.align(Alignment.CenterVertically))
                    IconButton(onClick = { /* Handle increment */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GradientImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Image
        Image(
            painter = painterResource(id = R.drawable.fork_1), // Replace with your image
            contentDescription = "Product Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f), // Dark top
                            Color.Transparent // Fades to transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(4) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700) // Gold color for stars
                    )
                }
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Color.Gray // Half star or empty star
                )
            }
            // Text on Top of Gradient
            Text(
                text = "One Shoulder Linen Dress",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            // Size Selection
            Text(
                text = "Size",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                listOf("UK 8", "UK 10", "UK 12").forEach { size ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = size, color = Color.Black)
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun ProductDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Section: Image and Back Button
        Box {
            Image(
                painter = painterResource(id = R.drawable.fork_1), // Replace with your image
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { /* Handle back button */ },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Info Section
        Column(modifier = Modifier.padding(16.dp)) {
            // Product Name
            Text(
                text = "One Shoulder Linen Dress",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Rating and Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Star Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(4) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700) // Gold color
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Half Star",
                        tint = Color(0xFFFFD700)
                    )
                }
                // Price
                Text(
                    text = "Rs. 5740",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Size Selection
            Text(
                text = "Size",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                listOf("UK 8", "UK 10", "UK 12").forEach { size ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = size, color = Color.Black)
                    }
                }
            }

            // Quantity Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Color", fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { /* Handle decrement */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Decrease")
                    }
                    Text(text = "1", modifier = Modifier.align(Alignment.CenterVertically))
                    IconButton(onClick = { /* Handle increment */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            // Color Options
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Color(0xFFFFCDD2),
                    Color(0xFFB2EBF2),
                    Color(0xFFB2DFDB),
                    Color(0xFFFFF176)
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // Specification Section
            Text(
                text = "Specification",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dress\nMaterial: Linen\nMaterial Composition: 100% Linen",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please bear in mind that the photo may be slightly different from the actual item in terms of color due to lighting conditions or the display used to view.",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Section: Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle buy now */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081))
            ) {
                Text(text = "Buy now", color = Color.White)
            }
            Button(
                onClick = { /* Handle add to cart */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Add to Cart", color = Color.White)
            }
        }
    }
}




