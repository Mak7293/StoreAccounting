package com.example.storeaccounting.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.presentation.General.General
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.inventory.Inventory
import com.example.storeaccounting.presentation.sale.Sale
import com.example.storeaccounting.presentation.setting.Setting
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.ui.theme.StoreAccountingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreAccountingTheme {
                val navController = rememberNavController()


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
                            Inventory()
                        }
                        composable(route = NavigationRoute.Sale.route){
                            Sale()
                        }
                        composable(route = NavigationRoute.Setting.route){
                            Setting()
                        }
                    }
                }

            }
        }
    }
}

