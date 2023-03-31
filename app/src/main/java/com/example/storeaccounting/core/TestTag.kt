package com.example.storeaccounting.core

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

object TestTag {
    const val INVENTORY_NAME = "inventory_name"
    const val INVENTORY_NUMBER = "inventory_number"
    const val INVENTORY_BUY_PRICE = "inventory_buy_price"
    const val INVENTORY_SELL_PRICE = "inventory_sell_price"
    const val INVENTORY_ADD = "inventory_add"

    const val SALE_NAME = "sale_name"
    const val SALE_ADD = "sale_add"
    const val SALE_ITEM_COLUMN = "sale_item_column"

    val SaleKey = SemanticsPropertyKey<String>("saleKey")
    var SemanticsPropertyReceiver.saleTitle by SaleKey
}