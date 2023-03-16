package com.example.storeaccounting.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.presentation.General.General
import com.example.storeaccounting.presentation.add_edit_credit_card.AddEditCreditCard
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.inventory.AddEditInventoryBottomSheetContent
import com.example.storeaccounting.presentation.inventory.Inventory
import com.example.storeaccounting.presentation.sale.ResultBottomSheetContent
import com.example.storeaccounting.presentation.sale.Sale
import com.example.storeaccounting.presentation.sale.SaleBottomSheetContent
import com.example.storeaccounting.presentation.setting.Setting
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import com.razaghimahdi.compose_persian_date.core.rememberPersianDataPicker
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

                SetStatusBarTheme(
                    window = window,
                    currentFragment = ""
                )

                //SetStatusBarTheme(window, currentFragment)

                /*val persianDate = PersianDate()
                val persianDateFormatter = PersianDateFormat("Y/m/d")
                persianDateFormatter.format(persianDate)
                Log.d("today","year:${persianDate.shYear}, month:${persianDate.shMonth} && " +
                        "${persianDate.monthName}, day:${persianDate.shDay} && ${persianDate.dayFinglishName()}")
                Log.d("today formatted", persianDate.toString())
                Log.d("today formatted!!", persianDateFormatter.format(persianDate).toString())*/

                val navController = rememberNavController()
                val parentNavController = rememberNavController()
                NavHost(
                    navController = parentNavController,
                    startDestination = NavigationRoute.Main.route,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                ){
                    composable(route = NavigationRoute.Main.route){
                        Main(
                            navController = navController,
                            parentNavController = parentNavController,
                            window = window
                        )
                    }
                    composable(route = NavigationRoute.AddEditCreditCard.route){

                        AddEditCreditCard( parentNavController = parentNavController)
                        SetStatusBarTheme(window,it.destination.route!!)
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main(
    navController: NavHostController,
    parentNavController: NavHostController,
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    window: Window
) {
    Log.d("MainRecomposition","@@@@@@@@")
    val fabState = remember {
        mutableStateOf<String>("")
    }
    var inventory by remember {
        mutableStateOf<InventoryEntity?>(null)
    }
    var editSaleHistory by remember {
        mutableStateOf<History?>(null)
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
        inventorySaleViewModel.eventFlow.collectLatest { event  ->
            when(event){
                is  InventorySaleViewModel.UiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message,Toast.LENGTH_SHORT).show()

                }
                is  InventorySaleViewModel.UiEvent.SaveInventory  ->  {
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.UiEvent.DeleteInventory   -> {
                    Toast.makeText(context,"کالا با موفقیت حذف شد.",Toast.LENGTH_SHORT).show()
                }
                is InventorySaleViewModel.UiEvent.UpdateInventory   -> {
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.UiEvent.SaleInventory   ->   {
                    Toast.makeText(context,"فروش کالا با موفقیت ثبت شد.",Toast.LENGTH_SHORT).show()
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.UiEvent.UpdateSaleHistory  ->  {
                    Toast.makeText(context,"تراکنش فروش با موقیت به روز رسانی شد.",Toast.LENGTH_SHORT).show()
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.UiEvent.DeleteSaleHistory   ->   {
                    Toast.makeText(context,"تراکنش فروش با موقیت به حذف شد.",Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState ,
        sheetContent = {
            when(fabState.value){
                FabRoute.InventoryFab.route ->  {
                    AddEditInventoryBottomSheetContent(
                        modifier = Modifier
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
                        inventory = inventory,
                        inventorySaleViewModel = inventorySaleViewModel,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.onSurface,
                                        MaterialTheme.colors.secondary
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black,
                                shape = RoundedCornerShape(
                                    topStart = 35.dp,
                                    topEnd = 35.dp
                                ),
                            ),
                        saleList = inventorySaleViewModel.state.value.inventory,
                        oldHistory = null
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
                FabRoute.EditSaleFab.route   ->   {
                    Log.d("history!!", editSaleHistory.toString())
                    SaleBottomSheetContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.onSurface,
                                        MaterialTheme.colors.secondary
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black,
                                shape = RoundedCornerShape(
                                    topStart = 35.dp,
                                    topEnd = 35.dp
                                ),
                            ),
                        saleList = inventorySaleViewModel.state.value.inventory,
                        oldHistory = editSaleHistory
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
                FabRoute.ResultFab.route   ->  {
                    ResultBottomSheetContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.onSurface,
                                        MaterialTheme.colors.secondary
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSystemInDarkTheme()) Color.DarkGray else Color.Black,
                                shape = RoundedCornerShape(
                                    topStart = 35.dp,
                                    topEnd = 35.dp
                                ),
                            ),
                        controller = rememberPersianDataPicker(),
                        onCancel = {
                            scope.launch {
                                if(sheetState.isCollapsed){
                                    sheetState.expand()
                                }else{
                                    sheetState.collapse()
                                }
                            }
                        },
                        onResult = {
                            scope.launch {
                                if(sheetState.isCollapsed){
                                    sheetState.expand()
                                }else{
                                    sheetState.collapse()
                                }
                            }
                        }
                    )
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
        sheetGesturesEnabled = false,
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
                    General(
                        parentNavController = parentNavController,
                    )
                    SetStatusBarTheme(window,it.destination.route!!)
                }
                composable(route = NavigationRoute.Inventory.route){
                    Inventory{ route , _invnetory   ->

                        fabState.value = route.route
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()

                            } else {
                                sheetState.collapse()
                            }
                        }
                        inventory = _invnetory
                    }
                    SetStatusBarTheme(window,it.destination.route!!)
                }
                composable(route = NavigationRoute.Sale.route){
                    Sale(
                        inventorySaleViewModel = inventorySaleViewModel,
                        onEdit = {   History, FabRoute  ->
                            fabState.value = FabRoute.route
                            editSaleHistory = History
                            scope.launch {
                                if(sheetState.isCollapsed) {
                                    sheetState.expand()
                                } else {
                                    sheetState.collapse()
                                }
                            }
                        }
                    ){

                        fabState.value = it.route
                        scope.launch {
                            if(sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                    SetStatusBarTheme(window,it.destination.route!!)
                }
                composable(route = NavigationRoute.Setting.route){
                    Setting()
                    SetStatusBarTheme(window,it.destination.route!!)
                }
            }
        }
    }
}

@Composable
fun SetStatusBarTheme(window : Window, currentFragment: String) {
    when(currentFragment){
        NavigationRoute.Sale.route  ->   {
            if (isSystemInDarkTheme()) {
                val backgroundArgb = MaterialTheme.colors.primary.toArgb()
                window.statusBarColor = backgroundArgb
                /* val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
                 windowInsetController?.isAppearanceLightStatusBars = true*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    window.insetsController?.setSystemBarsAppearance(
                        APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
                }else{
                    window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

                }
            } else {
                val backgroundArgb = MaterialTheme.colors.primary.toArgb()
                window.statusBarColor = backgroundArgb
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    window.insetsController?.setSystemBarsAppearance(
                        0, APPEARANCE_LIGHT_STATUS_BARS)
                }else{
                    window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
                }
            }
        }
        NavigationRoute.AddEditCreditCard.route  ->  {
            if (isSystemInDarkTheme()) {
                val backgroundArgb = MaterialTheme.colors.onSurface.toArgb()
                window.statusBarColor = backgroundArgb
                /*val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
                windowInsetController?.isAppearanceLightStatusBars = false*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    window.insetsController?.setSystemBarsAppearance(
                        0, APPEARANCE_LIGHT_STATUS_BARS)
                }else{
                    window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            } else {
                val backgroundArgb = MaterialTheme.colors.onSurface.toArgb()
                window.statusBarColor = backgroundArgb
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    window.insetsController?.setSystemBarsAppearance(
                        APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
                }else{
                    window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
        }
        else   ->   {
            if (isSystemInDarkTheme()) {
                val backgroundArgb = MaterialTheme.colors.background.toArgb()
                window.statusBarColor = backgroundArgb
                /*val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
                windowInsetController?.isAppearanceLightStatusBars = false*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    window.insetsController?.setSystemBarsAppearance(
                        0, APPEARANCE_LIGHT_STATUS_BARS)
                }else{
                    window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
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
    }
}




