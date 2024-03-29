package com.example.storeaccounting.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationRoute(
    val route: String,
    val icon: ImageVector?,
    val title: String?
) {
    object Inventory: NavigationRoute(
        route = "inventory",
        icon = Icons.Outlined.Inventory,
        title = "کالا و انبار"
    )
    object Sale: NavigationRoute(
        route = "sale",
        icon = Icons.Outlined.Sell,
        title = "فروش"
    )
    object General: NavigationRoute(
        route = "general",
        icon = Icons.Outlined.Home,
        title = "اشخاص"
    )
    object Setting: NavigationRoute(
        route = "setting",
        icon = Icons.Outlined.Settings,
        title = "تنظیمات"
    )
    object AddEditCreditCard: NavigationRoute(
        route = "add_credit_card",
        icon = null,
        title = null
    )
    object Main: NavigationRoute(
        route = "main",
        icon = null,
        title = null
    )
    object AddEditFactor: NavigationRoute(
        route = "add_edit_factor",
        icon = null,
        title = null
    )
    object SplashScreen: NavigationRoute(
        route = "splash_screen",
        icon = null,
        title = null
    )
}