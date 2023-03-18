package com.example.storeaccounting.presentation.add_edit_factor

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.presentation.add_edit_credit_card.AddEditCreditCardTopBar
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.general.GeneralEvent
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel

@Composable
fun AddEditFactor(
    parentNavController: NavController,
    context: Context = LocalContext.current,
    viewModel: GeneralViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            AddEditFactorTopBar(
                onBackPressed = {
                    parentNavController.popBackStack(route = NavigationRoute.Main.route, inclusive = false)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // do something
                },
                backgroundColor = MaterialTheme.colors.primary
            ){
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Default.Save,
                    contentDescription = "create",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ){
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "add_edit_factor",
                color = Color.LightGray
            )
        }
    }


}
@Composable
fun AddEditFactorTopBar(
    onBackPressed: ()-> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "ساخت فاکتور",
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