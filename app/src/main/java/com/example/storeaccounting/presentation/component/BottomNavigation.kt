package com.example.storeaccounting.presentation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.storeaccounting.presentation.util.NavigationRoute
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeaccounting.ui.theme.persian_font_medium


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val menuItems = listOf(
        NavigationRoute.Inventory,
        NavigationRoute.Sale,
        NavigationRoute.General,
        NavigationRoute.Setting,
    )
    BottomNavigation(
        contentColor = MaterialTheme.colors.surface,
        backgroundColor = MaterialTheme.colors.onSurface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            BottomNavigationItem(
                label = {
                    Text(
                        text = it.title!!,
                        fontSize = if(currentRoute == it.route) 14.sp else 12.sp,
                        fontFamily = persian_font_medium
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.surface,
                unselectedContentColor = MaterialTheme.colors.secondaryVariant,
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route){
                        navController.graph.startDestinationRoute?.let{ route  ->
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(
                                if(currentRoute == it.route){
                                    30.dp
                                }else{
                                    25.dp
                                }
                            ),
                        imageVector = it.icon!!,
                        contentDescription = it.title,

                    )
                },
            )
        }
    }
}

