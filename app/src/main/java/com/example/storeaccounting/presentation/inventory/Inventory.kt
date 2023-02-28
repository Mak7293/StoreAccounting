package com.example.storeaccounting.presentation.inventory

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.view_model.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun Inventory(
    viewModel: MainViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    onClick: (FabRoute) ->  Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    onClick(FabRoute.InventoryFab)
                    Toast(context).apply {
                        setText("Clicked!!")
                        duration = Toast.LENGTH_SHORT
                    }.show()
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
        InventoryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        )
    }

}



@Composable
fun InventoryContent(
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(text = "Inventory Tab")
    }
}