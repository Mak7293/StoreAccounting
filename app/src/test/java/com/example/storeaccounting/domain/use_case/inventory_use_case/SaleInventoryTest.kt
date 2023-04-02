package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.data.repository.FakeRepository
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SaleInventoryTest{
    private lateinit var addInventory: AddInventory
    private lateinit var saleInventory: SaleInventory
    private lateinit var getInventory: GetInventory
    private lateinit var getHistory: GetHistory
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        getInventory = GetInventory(fakeRepository)
        addInventory = AddInventory(fakeRepository)
        getHistory = GetHistory(fakeRepository)
        saleInventory = SaleInventory(fakeRepository)

        val inventoryToInsert = mutableListOf<InventoryEntity>()
        ('a' .. 'z').forEachIndexed{ index, c ->
            inventoryToInsert.add(
                InventoryEntity(
                    title = c.toString(),
                    date = c.toString(),
                    timeStamp = System.currentTimeMillis(),
                    createdTimeStamp = System.currentTimeMillis(),
                    buyPrice =(c.code*(10..15).random()).toString(),
                    sellPrice = (c.code*(15..20).random()).toString(),
                    number = (c.code*(5..10).random()).toString()
                )
            )
        }
        inventoryToInsert.shuffle()
        runBlocking {
            inventoryToInsert.forEach{
                addInventory(it)
            }
        }
    }
    @Test
    fun saleInventory_s() = runBlocking{
        val string = "string"
        val inventory = InventoryEntity(
            title = string,
            date = string,
            timeStamp = System.currentTimeMillis(),
            createdTimeStamp = System.currentTimeMillis(),
            buyPrice =(string.length*(10..15).random()).toString(),
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(5..10).random()).toString()
        )
        // test add inventory
        addInventory(inventory)
        val inventories = getInventory().first()
        var insertTest: Boolean = false
        for(i in inventories){
            if(inventory.title == i.title && inventory.date == i.date && inventory.createdTimeStamp == i.createdTimeStamp
                && inventory.timeStamp == i.timeStamp && inventory.buyPrice == i.buyPrice && inventory.sellPrice==i.sellPrice
                && inventory.number==i.number){
                insertTest = true
            }
        }
        assert(insertTest)
        // test update inventory
        if(insertTest){
            val sellNumber = 4
            val newInventory = inventory.copy(
                timeStamp = System.currentTimeMillis(),
                number = (inventory.number.toLong() - sellNumber).toString()
            )
            val newHistory = History(
                title = newInventory.title,
                timeStamp = System.currentTimeMillis(),
                sellPrice = newInventory.sellPrice,
                buyPrice = newInventory.buyPrice,
                date = newInventory.date,
                saleNumber = sellNumber.toString(),
                createdTimeStamp = newInventory.createdTimeStamp,
                remainingInventory = newInventory.number.toLong(),
                transaction = TransactionState.Sale.state
            )
            saleInventory(
                inventoryEntity = newInventory,
                history = newHistory
            )
            val inventories = getInventory().first()
            val histories = getHistory().first()
            var inventoryTest = false
            for (i in inventories){
                if(i.createdTimeStamp == newInventory.createdTimeStamp &&
                            i.number == (inventory.number.toLong() - sellNumber).toString()
                ){
                    inventoryTest = true
                    break
                }
            }
            assert(inventoryTest)
            var historyTest = false
            for (i in histories){
                if(i.createdTimeStamp == newHistory.createdTimeStamp &&
                    i.remainingInventory.toString() == (inventory.number.toLong() - sellNumber).toString() &&
                            i.transaction == TransactionState.Sale.state
                ){
                    historyTest = true
                }
            }
            assert(historyTest)
        }
    }
}