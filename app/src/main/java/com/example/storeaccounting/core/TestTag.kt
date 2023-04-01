package com.example.storeaccounting.core

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

object TestTag {
    const val INVENTORY_NAME = "inventory_name"
    const val INVENTORY_NUMBER = "inventory_number"
    const val INVENTORY_BUY_PRICE = "inventory_buy_price"
    const val INVENTORY_SELL_PRICE = "inventory_sell_price"
    const val INVENTORY_ADD = "inventory_add"

    const val SALE_NUMBER = "sale_name"
    const val SALE_ADD = "sale_add"
    const val SALE_ITEM_BOTTOM_SHEET_LAZY_COLUMN = "sale_item_bottom_sheet_lazy_column"
    const val INVENTORY_ITEM_LAZY_COLUMN = "inventory_item_lazy_column"
    const val SALE_ITEM_LAZY_COLUMN = "sale_item_lazy_column"

    val SaleKey = SemanticsPropertyKey<String>("saleKey")
    var SemanticsPropertyReceiver.saleTitle by SaleKey

    val InventoryLazyKey = SemanticsPropertyKey<String>("inventoryLazyKey")
    var SemanticsPropertyReceiver.inventoryLazyTitle by InventoryLazyKey

    val SaleLazyKey = SemanticsPropertyKey<String>("saleLazyKey")
    var SemanticsPropertyReceiver.saleLazyTitle by SaleLazyKey
}