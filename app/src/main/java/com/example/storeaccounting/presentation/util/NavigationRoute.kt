package com.example.storeaccounting.presentation.util

sealed class NavigationRoute(val route: String) {
    object Inventory: NavigationRoute("inventory")
    object Sale: NavigationRoute("sale")
    object General: NavigationRoute("general")
    object Setting: NavigationRoute("setting")
}