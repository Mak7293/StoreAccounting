package com.example.storeaccounting.core

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import com.example.storeaccounting.presentation.view_model.general.GeneralViewModel
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleEvent
import com.example.storeaccounting.presentation.view_model.inventory_sale.InventorySaleViewModel
import saman.zamani.persiandate.PersianDateFormat

object FakeEntries {
    private var fakeList: MutableList<InventoryEntity>? = null
    private fun fakeInventory():List<InventoryEntity>{
        val list: MutableList<InventoryEntity> = mutableListOf()
        ('a'..'z').forEachIndexed { index, c ->
            val inventoryEntity = InventoryEntity(
                id = null,
                date = index.toString(),
                title = c.toString(),
                number = (c.code * (5..10).random()).toString(),
                timeStamp = System.currentTimeMillis() - ("${index * 86_400}000").toLong(),
                createdTimeStamp = System.currentTimeMillis() - ("${index * 86_400}000").toLong(),
                sellPrice = (c.code * (2000..3000).random()).toString(),
                buyPrice = (c.code * (1000..2000).random()).toString()
            )
            list.add(inventoryEntity)
        }
        fakeList = list
        return list
    }
    private fun fakeSale():MutableList<Pair<InventoryEntity,History>>{
        val list: MutableList<Pair<InventoryEntity,History>> = mutableListOf()
        fakeList?.forEach {
            val number = (1..4).random().toString()
            val newInventoryEntity = it.copy(
                number = "${it.number.toLong() - number.toLong()}",
                timeStamp = it.timeStamp+10000,
            )
            val newHistory = History(
                id= null ,
                createdTimeStamp = newInventoryEntity.createdTimeStamp,
                remainingInventory = newInventoryEntity.number.toLong(),
                transaction = TransactionState.Sale.state,
                title = newInventoryEntity.title,
                saleNumber = number,
                buyPrice = newInventoryEntity.buyPrice,
                sellPrice = newInventoryEntity.sellPrice,
                timeStamp = newInventoryEntity.timeStamp,
                date = newInventoryEntity.date
            )
            list.add(Pair(newInventoryEntity,newHistory))
        }
        return list
    }
    fun fake(viewModel: InventorySaleViewModel){
        fakeInventory().forEach {
            viewModel.onEvent(InventorySaleEvent.InsertInventory(it))
        }
        fakeSale().forEach {
            viewModel.onEvent(InventorySaleEvent.SaleInventory(inventoryEntity = it.first, history = it.second))
        }
    }
}