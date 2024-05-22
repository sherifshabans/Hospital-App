package com.elsharif.hospitalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun DropdownExample(
        labelText: String,
        items: List<String>,
        paddingValue:PaddingValues
):Pair<String,String>{

        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
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
            onClick = { expanded = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop down")
        }

        Text(selectedItem, modifier = Modifier.padding(8.dp), maxLines = 2)



        Text(labelText,modifier=Modifier.padding(8.dp), maxLines = 2)
    }

        return selectedItem to labelText
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StartScreen(
    navController: NavController
) {

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
        ,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                val list1 = listOf(
                    "قسم الطوارئ و الاستقبال",
                    "ادارة منع ومكافحة العدوى",
                    "القسم الداخلي",
                    "العيادات الخارجيه",
                    "العناية المركزية",
                    "وحدة العناية المركزية لحديثي الولادة (المبتسرين)",
                    "جناح العمليات غرفة الافاقة",
                    "قائمة المناظير",
                    "وحدة الغسيل الكلوي",
                    "الصيدلية الاكلينيكية",
                    "الاسنان",
                    "المعمل",
                    "بنك الدم",
                    "الاشعة",
                    "التعقيم",
                    "المطبخ",
                    "المغسلة",
                    "معالجة النفايات",
                    "المشرحة"
                )
                val list2 = listOf("جامعة أسيوط")

                val list3 = listOf("المستشفى الرئيسي","مستشفى الاطفال","مستشفى الأعصاب والطب النفسي","مستشفى المسالك البولية"
                ,"مستشفى المرأة"
                ,"مستشفى أمراض القلب","مستشفى الراجحي")
                val selectedItem2 = DropdownExample("اختر اسم الجامعة ", list2,it)

                val selectedItem3 = DropdownExample("اختر اسم المستشفى ", list3,it)
                val selectedItem = DropdownExample("اختر عنصر التحقيق ", list1,it)

                Spacer(modifier = Modifier.height(30.dp))



                Column(
                    modifier=Modifier.fillMaxWidth().padding(10.dp),
                   verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {


                Button(onClick = {
                    if(selectedItem!=null &&selectedItem2!=null&&selectedItem3!=null){

                    navController.navigate(
                        "CheckListScreen/${
                            selectedItem.first
                        }/${
                            selectedItem2.first
                        }/${
                            selectedItem3.first
                        }"
                    )
                    }

                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                    Text(text = "التالي")

                }}
            }


        })
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
