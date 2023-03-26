package com.example.storeaccounting.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.presentation.General.General
import com.example.storeaccounting.presentation.add_edit_credit_card.AddEditCreditCardTopBar
import com.example.storeaccounting.presentation.add_edit_factor.AddEditFactor
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.inventory.AddEditInventoryBottomSheetContent
import com.example.storeaccounting.presentation.inventory.Inventory
import com.example.storeaccounting.presentation.sale.ResultBottomSheetContent
import com.example.storeaccounting.presentation.sale.Sale
import com.example.storeaccounting.presentation.sale.SaleBottomSheetContent
import com.example.storeaccounting.presentation.setting.Setting
import com.example.storeaccounting.presentation.util.Constants.CREDIT_CARD_ID
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.util.ThemeState
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import com.example.storeaccounting.presentation.view_model.setting.SettingViewModel
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import com.razaghimahdi.compose_persian_date.core.rememberPersianDataPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            StoreAccountingTheme {

                val settingViewModel = viewModel<SettingViewModel>()
                LaunchedEffect(key1 = true){
                    settingViewModel.readCurrentThemeForDataStore().collectLatest { theme ->
                        when(theme){
                             ThemeState.ThemeNight.theme  ->  {
                                 Log.d("ThemeNight","aa")
                                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                             }
                             ThemeState.ThemeDay.theme  ->  {
                                 Log.d("ThemeDay","aa")
                                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                             }
                             ThemeState.ThemeDefault.theme  ->  {
                                 Log.d("ThemeDefault","aa")
                                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                             }
                        }
                    }
                }

                SetStatusBarTheme(
                    window = window,
                    currentFragment = ""
                )

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
                    composable(
                        route = "${NavigationRoute.AddEditCreditCard.route}/{$CREDIT_CARD_ID}",
                        arguments = listOf(
                            navArgument(CREDIT_CARD_ID){
                                type = NavType.IntType
                                this.defaultValue = -1
                            }
                        )
                    ){
                        SetStatusBarTheme(window,it.destination.route!!.split("/").first())
                        AddEditCreditCardTopBar(
                            parentNavController = parentNavController,
                            editCardId = it.arguments?.getInt(CREDIT_CARD_ID)
                        )
                    }
                    composable(
                        route = NavigationRoute.AddEditFactor.route,
                        /*arguments = listOf(
                            navArgument(CREDIT_CARD_ID){
                                type = NavType.IntType
                                this.defaultValue = -1
                            }
                        )*/
                    ){
                        SetStatusBarTheme(window,it.destination.route!!.split("/").first())
                        AddEditFactor(
                            parentNavController = parentNavController,
                            //editCardId = it.arguments?.getInt(CREDIT_CARD_ID)
                        )
                    }
                }

            }
        }
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main(
    navController: NavHostController,
    parentNavController: NavHostController,
    inventorySaleViewModel: InventorySaleViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    window: Window
) {
    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                is  InventorySaleViewModel.InventorySaleUiEvent.ShowToast   ->   {
                    Toast.makeText(context,event.message,Toast.LENGTH_SHORT).show()

                }
                is  InventorySaleViewModel.InventorySaleUiEvent.SaveInventory  ->  {
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.DeleteInventory   -> {
                    Toast.makeText(context,"کالا با موفقیت حذف شد.",Toast.LENGTH_SHORT).show()
                }
                is InventorySaleViewModel.InventorySaleUiEvent.UpdateInventory   -> {
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.SaleInventory   ->   {
                    Toast.makeText(context,"فروش کالا با موفقیت ثبت شد.",Toast.LENGTH_SHORT).show()
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.UpdateSaleHistory  ->  {
                    Toast.makeText(context,"تراکنش فروش با موقیت به روز رسانی شد.",Toast.LENGTH_SHORT).show()
                    scope.launch {
                        if(sheetState.isCollapsed) {
                            sheetState.expand()
                        } else {
                            sheetState.collapse()
                        }
                    }
                }
                is InventorySaleViewModel.InventorySaleUiEvent.DeleteSaleHistory   ->   {
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
        NavigationRoute.AddEditFactor.route  ->  {
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




