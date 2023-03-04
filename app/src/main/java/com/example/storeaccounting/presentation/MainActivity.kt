package com.example.storeaccounting.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.presentation.General.General
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.inventory.AddToInventoryBottomSheetContent
import com.example.storeaccounting.presentation.inventory.Inventory
import com.example.storeaccounting.presentation.sale.Sale
import com.example.storeaccounting.presentation.setting.Setting
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.inventory.inventory_view_model.InventoryViewModel
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreAccountingTheme {
                SetStatusBarTheme(window)

                /*val persianDate = PersianDate()
                val persianDateFormatter = PersianDateFormat("Y/m/d")
                persianDateFormatter.format(persianDate)
                Log.d("today","year:${persianDate.shYear}, month:${persianDate.shMonth} && " +
                        "${persianDate.monthName}, day:${persianDate.shDay} && ${persianDate.dayFinglishName()}")
                Log.d("today formatted", persianDate.toString())
                Log.d("today formatted!!", persianDateFormatter.format(persianDate).toString())*/

                val navController = rememberNavController()
                Main(
                    navController = navController,

                )
            }
        }
    }
}

@Composable
fun SetStatusBarTheme(window : Window) {
    if (isSystemInDarkTheme()) {
        val backgroundArgb = MaterialTheme.colors.background.toArgb()
        window.statusBarColor = backgroundArgb
    } else {
        val backgroundArgb = MaterialTheme.colors.background.toArgb()
        window.statusBarColor = backgroundArgb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.setSystemBarsAppearance(
                APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)

        }else{
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main(
    navController: NavHostController,
    viewModel: InventoryViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val fabState = remember {
        mutableStateOf<String>("")
    }
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        if (sheetState.isExpanded){
            sheetState.collapse()
        }
    }
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event  ->
            when(event){
                is  InventoryViewModel.UiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message,Toast.LENGTH_SHORT).show()

                }
                is  InventoryViewModel.UiEvent.SaveNote  ->  {
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
            }

        }
    }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState ,
        sheetContent = {
            when(fabState.value){
                FabRoute.InventoryFab.route ->  {
                    AddToInventoryBottomSheetContent(
                        Modifier
                            .fillMaxWidth()
                            .height(440.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.onSurface,
                                        MaterialTheme.colors.secondary
                                    )
                                )
                            ),
                        viewModel
                    ){
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()

                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                }
                FabRoute.SaleFab.route   ->   {
                    SaleBottomSheetContent(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.secondary,
                                        MaterialTheme.colors.onSurface
                                    )
                                )
                            )
                    ){
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()

                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = "")
                    }
                }
            }
        },
        sheetBackgroundColor = Color.Transparent,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 35.dp,
            topEnd = 35.dp
        ),
        sheetGesturesEnabled = false
    ){

        Scaffold(
            bottomBar = {
                BottomNavigation(navController = navController)
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.General.route,
                modifier = Modifier
                    .padding(paddingValues = it)
                    .background(MaterialTheme.colors.background)
            ){
                composable(route = NavigationRoute.General.route){
                    General()
                }
                composable(route = NavigationRoute.Inventory.route){
                    Inventory(){
                        fabState.value = it.route
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()

                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                }
                composable(route = NavigationRoute.Sale.route){
                    Sale(){
                        fabState.value = it.route
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()

                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                }
                composable(route = NavigationRoute.Setting.route){
                    Setting()
                }
            }
        }
    }
}


@Composable
fun SaleBottomSheetContent(
    modifier: Modifier = Modifier,
    onClick:()  ->  Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Button(onClick = {
            onClick()
        }) {
            Text(
                text = "close Sale Bottom sheet",
                fontSize = 18.sp
            )
        }
    }
}



