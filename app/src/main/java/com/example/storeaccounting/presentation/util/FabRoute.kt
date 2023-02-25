package com.example.storeaccounting.presentation.util

sealed class FabRoute(val route: String){
    object InventoryFab: FabRoute(route = "inventory")
    object SaleFab: FabRoute(route = "route")
}
