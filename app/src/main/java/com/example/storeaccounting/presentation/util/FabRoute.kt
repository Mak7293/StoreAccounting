package com.example.storeaccounting.presentation.util

sealed class FabRoute(val route: String){
    object InventoryFab: FabRoute(route = "inventory")
    object SaleFab: FabRoute(route = "route")
    object ResultFab: FabRoute(route = "result_fab")
    object EditSaleFab: FabRoute(route = "edit_sale_fab")
}
