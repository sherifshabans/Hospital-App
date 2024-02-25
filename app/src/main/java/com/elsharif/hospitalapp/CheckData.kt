package com.elsharif.hospitalapp

import android.content.Context


import kotlinx.serialization.json.Json

object CheckData {

   // val jsonString = Context.assets.open("checkList.json").bufferedReader().use { it.readText() }

    //val checkListItems = Gson().fromJson(jsonString, Array<CheckList>::class.java).toList()

/*
    val jsonString = assets.open("checkList.json").bufferedReader().use { it.readText() }
    val checkListItems = Json.decodeFromString<List<CheckList>>(jsonString)
*/

    var checkListItems: List<CheckList> = listOf()


    fun initList(context: Context): List<CheckList> {
        if (checkListItems.isEmpty()) {
            val products =
                context.assets.open("checkList.json").bufferedReader().use { it.readText() }
            checkListItems = Json.decodeFromString(products)
        }
        return checkListItems
    }
    fun getCheckListByName(name: String): CheckList? {
        return checkListItems.find { it.checkList == name }
    }

}
/*package com.example.quickmart.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickmart.model.CartItem
import com.example.quickmart.model.Product
import com.google.gson.Gson


object ProductRepository {
    private var productList: List<Product> = emptyList()

    fun initProducts(context : Context) {
        val productsJson = context.assets.open("products.json").bufferedReader().use { it.readText() }
        productList = Gson().fromJson(productsJson, Array<Product>::class.java).toList()
    }

    fun getProducts(name: String, category: String = "All"): List<Product> {
        return productList.filter {
            (name.isEmpty() || it.title.contains(name, ignoreCase = true)) &&
                    (category == "All" || it.category.equals(category, ignoreCase = true))
        }
    }

    fun getProductCategories(context: Context): List<String> {
        val categoriesJson = context.assets.open("product-categories.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(categoriesJson, Array<String>::class.java).toList()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductListScreen(navController: NavController) {
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf("All") }
    var isDropdownExpanded = remember { mutableStateOf(false) }
    val categories = ProductRepository.getProductCategories(context)

    val Purple80 = Color(0xFF81A35A)
    val PurpleGrey80 = Color(0xFF465A2E)
    val Pink80 = Color(0xFF557E25)

    val Purple40 = Color(0xFF4D5742)
    val PurpleGrey40 = Color(0xFF8BC34A)
    val Pink40 = Color(0xFF2E4D0A)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product List") },
                backgroundColor = PurpleGrey40, // Set the desired background color here
                contentColor = Color.White, // Set the text color to white
                elevation = 4.dp, // Add elevation to the app bar
                actions = {
                    // Category filter dropdown
                    DropdownMenu(
                        expanded = isDropdownExpanded.value,
                        onDismissRequest = { isDropdownExpanded.value = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory.value = category
                                    isDropdownExpanded.value = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = category)
                            }
                        }
                    }
                    IconButton(
                        onClick = { isDropdownExpanded.value = !isDropdownExpanded.value },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Filter",
                            tint = if (isDropdownExpanded.value) Color.Red else Color.White
                        )
                    }

                    // Search field
                    BasicTextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        textStyle = TextStyle(color = Color.Black),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = PurpleGrey40, // Set the desired background color for the bottom bar
                contentColor = Color.White, // Set the text color to white
                elevation = 4.dp, // Add elevation to the bottom bar
                cutoutShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), // Add rounded corners to the bottom bar
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = {


                            navController.navigate("cart") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart Icon")
                        Text(text = "")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = { /* Handle the Home button click */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.LightGray) // Set the desired background color here
                .padding(16.dp) // Add padding to the entire content
        ) {
            // Product list
            val products = ProductRepository.getProducts(searchQuery.value, selectedCategory.value)
            LazyVerticalGrid(
                  GridCells.Fixed(2), // Number of columns
                modifier = Modifier.fillMaxSize()
                   , contentPadding = PaddingValues(16.dp, 16.dp, 0.dp, 16.dp) // Add padding as needed
            ) {
                items(products) { product ->
                    ProductItem(product = product, context = context)
                }
            }
        }
    }
}
@Composable
fun ProductItem(product: Product, context: Context) {
    val cartItems = CartRepository.getCartItems()

    // Find the cart item for this product in the cart
    val cartItem = cartItems.find { it.productName == product.title }

    val quantity = if (cartItem != null) {

        // If the product is in the cart, set the quantity to the cart's quantity
        remember { mutableStateOf(cartItem.quantity.toInt()) }
    } else {
        remember { mutableStateOf(0) }
    }
   val quantity2 = if (cartItem != null) {

        // If the product is in the cart, set the quantity to the cart's quantity
        remember { mutableStateOf(cartItem.quantity.toInt()) }
    } else {
        remember { mutableStateOf(0) }
    }

    val isAddedToCart = quantity.value > 0

    // Use remember to keep track of the selected state
    var isSelected by remember { mutableStateOf(false) }

    // Use Modifier.clickable to handle the click event and update the selected state
    val cardModifier = Modifier.clickable {
        isSelected = !isSelected
    }
    // Customize the appearance and layout for a single product item
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(IntrinsicSize.Max)
            .shadow(4.dp, shape = RoundedCornerShape(4.dp)) // Add a shadow here
            .then(cardModifier) // Apply the click handler modifier
            .border(2.dp, if (isSelected) Color.LightGray else Color.Transparent) // Add a border when selected

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val imageId =
                context.resources.getIdentifier(product.imageName, "drawable", context.packageName)

            // Load the image from drawable if it exists
           if(imageId>0) {


               Image(
                   painter = painterResource(id = imageId),
                   contentDescription = null,
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(150.dp)
                       .aspectRatio(1f)
               )
           }
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = product.category,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "$${product.price}",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (quantity.value > 0) {
                            quantity2.value--
                            CartRepository.addItem(
                                CartItem(
                                    product.title,
                                    quantity = quantity.value.toDouble()-1,
                                    productImage = product.imageName,
                                    price = product.price
                                )
                            )
                        }else {
                            quantity2.value=0
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Remove",
                    )
                }

                Text(
                    text = quantity2.value.toString(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(

                    onClick = {
                quantity2.value++
                         CartRepository.addItem(
                            CartItem(
                                product.title,
                                quantity =  quantity.value.toDouble()+1 ,
                                productImage = product.imageName,
                                price = product.price
                            )
                        )
                    }

                ) {
                    Icon(
                        Icons.Filled.AddCircle,
                        contentDescription = "Add to Cart",
                        tint = if (isAddedToCart) Color.Green else Color.Unspecified
                    )
                }
            }
        }
    }
}
*/