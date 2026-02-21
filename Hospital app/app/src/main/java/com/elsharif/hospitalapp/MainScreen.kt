package com.elsharif.hospitalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elsharif.hospitalapp.test.presentation.NotesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel:NotesViewModel
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.Home),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

        modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(1f)
                    ,
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Image Description",
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(it)
                        )
                    }
                }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(MaterialTheme.colorScheme.primary),

                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                val customCardColors = CardDefaults.cardColors(
                    containerColor = Color.Blue, // Change the background color here
                    contentColor = Color.Blue, // Change the content color here
                    disabledContainerColor = Color.Gray, // Change the disabled background color here
                    disabledContentColor = Color.LightGray // Change the disabled content color here
                )


                Card (
               modifier = Modifier
                   .fillMaxWidth()
                   .height(400.dp)
                   .padding(it)
                   .background(MaterialTheme.colorScheme.primary)
                   .shadow(4.dp)
                   .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
                    colors=customCardColors
           ){


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                IconButton(
                    onClick = {
                        navController.navigate("notes")
                    }

                ) {
                 Column(
                     modifier=Modifier.size(150.dp)
                 ){

                    Icon(Icons.Filled.Home, contentDescription = "الصفحة الرئيسية", modifier = Modifier.size(70.dp))
                     Spacer(modifier = Modifier.height(5.dp))
                     Text("الصفحة الرئيسية")

                 }

                }

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    onClick = {
                        viewModel.logout()
                        navController.navigate("LoginScreen") {
                            popUpTo("LoginScreen") { inclusive = true }
                        }
                    }
                ) {
                  Column {
                    Icon(Icons.Filled.Logout, contentDescription = "تسجيل خروج",modifier=Modifier.size(70.dp))
                    Spacer(modifier=Modifier.height(10.dp))
                    Text("تسجيل الخروج")
                  }
                }
            }
        }
           }
        }}
        }
    )
}
