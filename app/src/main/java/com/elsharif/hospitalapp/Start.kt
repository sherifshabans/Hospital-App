package com.elsharif.hospitalapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavController


@Composable
fun DropdownExample():String {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }

    val CheckListItems = listOf("قسم الطوارئ الاستقبال", "ادارة منع ومكافحة العدوى", "Item 3")

    Column {
        BasicTextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            modifier = Modifier.padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CheckListItems.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    expanded = false
                },
                    text = {
                        Text(text = item)
                    }
                )
                
            }
        }

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop down")
        }
    }
    return selectedItem
}

@Composable
fun StartScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        val selectedItem=DropdownExample()
        Spacer(modifier = Modifier.height(30.dp))


        Button(onClick = {

            navController.navigate("CheckListScreen/$selectedItem")

        } ,modifier= Modifier.fillMaxWidth()) {
            Text(text = "Done")
        
            Text(text = selectedItem)
        
        }
    }



}
/*
@Composable
fun DropdownExample(label: String, items: List<String>): String {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }

    Column {
        BasicTextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            modifier = Modifier.padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    expanded = false
                },
                    text = {
                        Text(text = item)
                    }
                )

            }
        }

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop down")
        }
    }
    return selectedItem
}

@Composable
fun StartScreen(
    navController: NavController
) {
    val labels = listOf("Dropdown 1", "Dropdown 2", "Dropdown 3", "Dropdown 4", "Dropdown 5")
    val items = List(5) { listOf("Item 1", "Item 2", "Item 3") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        val selectedItems = List(5) { i -> DropdownExample(labels[i], items[i]) }
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {

            navController.navigate("CheckListScreen/${selectedItems}")

        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Done")

            selectedItems.forEach {
                Text(text = it)
            }

        }
    }
}
*/
