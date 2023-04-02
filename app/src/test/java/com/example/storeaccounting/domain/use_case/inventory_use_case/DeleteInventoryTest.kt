package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.data.repository.FakeRepository
import com.example.storeaccounting.domain.model.InventoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteInventoryTest{
    private lateinit var addInventory: AddInventory
    private lateinit var deleteInventory: DeleteInventory
    private lateinit var getInventory: GetInventory
    private lateinit var getHistory: GetHistory
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        getInventory = GetInventory(fakeRepository)
        addInventory = AddInventory(fakeRepository)
        deleteInventory = DeleteInventory(fakeRepository)
        getHistory = GetHistory(fakeRepository)

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
    fun deleteInventory() = runBlocking{
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
        // test delete inventory
        assert(insertTest)
        if (insertTest){
            var deleteTest = true
            deleteInventory(inventory)
            val inventories = getInventory().first()
            for(i in inventories){
                if(inventory.title == i.title && inventory.date == i.date && inventory.createdTimeStamp == i.createdTimeStamp
                    && inventory.timeStamp == i.timeStamp && inventory.buyPrice == i.buyPrice && inventory.sellPrice==i.sellPrice
                    && inventory.number==i.number){
                    deleteTest = false
                }
            }
            assert(deleteTest)
            // test all history related to specific inventory must be deleted
            val histories = getHistory().first()
            for(i in histories){
                if (i.createdTimeStamp == inventory.createdTimeStamp){
                    assert(false)
                }
            }
        }
    }
}