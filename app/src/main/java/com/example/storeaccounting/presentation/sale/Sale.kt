package com.example.storeaccounting.presentation.sale

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.presentation.inventory.InventoryContent
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.MainViewModel

@Composable
fun Sale(
    viewModel: MainViewModel = hiltViewModel(),
    onClick:(FabRoute)  -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClick(FabRoute.SaleFab)
                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        SaleContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }

}

@Composable
fun SaleContent(
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(text = "Sale Tab")
    }
}