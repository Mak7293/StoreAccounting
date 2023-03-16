package com.example.storeaccounting.presentation.add_edit_credit_card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.storeaccounting.presentation.util.NavigationRoute

@Composable
fun AddEditCreditCard(
    parentNavController: NavController,
) {
    Scaffold(
        topBar = {
            AddEditCreditCard(
                onBackPressed = {
                     parentNavController.popBackStack(route = NavigationRoute.Main.route, inclusive = false)
                }
            )
        },
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ){
            Text(text = "Add_Edit_Credit_Card")
        }
    }
}

@Composable
fun AddEditCreditCard(
    onBackPressed: ()-> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "ایجاد کارت اعتباری",
                fontWeight = FontWeight.SemiBold)
        },
        backgroundColor = MaterialTheme.colors.onSurface,
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed()
                }
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}
