package com.example.storeaccounting.presentation

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.presentation.General.General
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.inventory.Inventory
import com.example.storeaccounting.presentation.sale.Sale
import com.example.storeaccounting.presentation.setting.Setting
import com.example.storeaccounting.presentation.util.FabRoute
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.presentation.view_model.MainViewModel
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import com.example.storeaccounting.ui.theme.persian_font_medium
import com.example.storeaccounting.ui.theme.persian_font_regular
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
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
    viewModel: MainViewModel = hiltViewModel()
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
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState ,
        sheetContent = {
            when(fabState.value){
                FabRoute.InventoryFab.route ->  {
                    InventoryBottomSheetContent(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colors.onSurface,
                                        MaterialTheme.colors.secondary
                                    )
                                )
                            ),
                        viewModel.getPersianDate()
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
fun InventoryBottomSheetContent(
    modifier: Modifier = Modifier,
    currentDate: PersianDate,
    onClick:()  ->  Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            RightToLeftLayout {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    textAlign = TextAlign.Center,
                    text = "نام و تعداد کالا را وارد کنید:",
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_regular,
                    fontSize = 18.sp
                )
            }
            EditText(
                hint = "نام کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp))
            EditText(
                hint = "تعداد کالا ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp))
            RightToLeftLayout {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 15.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Start

                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "date_icon")
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp),
                        text = PersianDateFormat("Y/m/d").format(currentDate).toString(),
                        color = MaterialTheme.colors.primaryVariant,
                        fontFamily = persian_font_regular,
                        fontSize = 18.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
                    
            ){
                Button(
                    modifier = Modifier.width(175.dp),
                    onClick = {
                        onClick()
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF013A63))

                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "خارج شدن",
                        color = Color.White,
                        fontFamily = persian_font_medium,
                        fontSize = 16.sp
                    )
                }
                Button(
                    modifier = Modifier.width(175.dp),
                    onClick = {
                        onClick()
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF008506))

                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "ذخیره کردن",
                        color = Color.White,
                        fontFamily = persian_font_medium,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    hint: String = ""
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(true)
    }
    RightToLeftLayout {
        Box(modifier = modifier){
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.primaryVariant,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.primaryVariant,
                    1.00f to MaterialTheme.colors.primaryVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent, CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                    .onFocusChanged {
                        if (text.isEmpty()) {
                            isHintDisplayed = !it.isFocused
                        }
                    }
            )
            if(isHintDisplayed){
                Text(
                    text = hint,
                    fontFamily = persian_font_medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(
                        horizontal = 20.dp ,
                        vertical = 12.dp
                    )
                )
            }
        }
    }

}

@Composable
fun RightToLeftLayout(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        content()
    }
}

