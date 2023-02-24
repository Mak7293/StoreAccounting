package com.example.storeaccounting.presentation.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.storeaccounting.presentation.util.NavigationRoute
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.storeaccounting.ui.theme.custom_gray_4


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
        contentColor = MaterialTheme.colors.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            BottomNavigationItem(
                label = {
                    Text(text = it.title!!)
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.onSurface,
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
                icon = { Icon(imageVector = it.icon!!, contentDescription = it.title) }
            )
        }
    }
}