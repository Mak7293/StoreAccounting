package com.example.storeaccounting.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.storeaccounting.presentation.component.BottomNavigation
import com.example.storeaccounting.presentation.util.NavigationRoute
import com.example.storeaccounting.ui.theme.StoreAccountingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreAccountingTheme {
                val navController = rememberNavController()
                
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
                    ){
                        composable(route = NavigationRoute.General.route){

                        }
                        composable(route = NavigationRoute.Inventory.route){

                        }
                        composable(route = NavigationRoute.Sale.route){

                        }
                        composable(route = NavigationRoute.Setting.route){

                        }
                    }
                }

            }
        }
    }
}
