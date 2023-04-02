package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.data.repository.FakeRepository
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateSaleHistoryTest{

    private lateinit var addInventory: AddInventory
    private lateinit var saleInventory: SaleInventory
    private lateinit var updateInventory: UpdateInventory
    private lateinit var updateSaleHistory: UpdateSaleHistory
    private lateinit var getInventory: GetInventory
    private lateinit var getHistory: GetHistory
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        getInventory = GetInventory(fakeRepository)
        addInventory = AddInventory(fakeRepository)
        saleInventory = SaleInventory(fakeRepository)
        getHistory = GetHistory(fakeRepository)
        updateInventory = UpdateInventory(fakeRepository)
        updateSaleHistory = UpdateSaleHistory(fakeRepository)

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
    fun updateSaleHistory_s() = runBlocking{
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
        // test sale inventory
        if(insertTest){
            val sellNumber = 4
            val inventory_1 = inventory.copy(
                timeStamp = System.currentTimeMillis(),
                number = (inventory.number.toLong() - sellNumber).toString()
            )
            val history_1 = History(
                title = inventory_1.title,
                timeStamp = System.currentTimeMillis(),
                sellPrice = inventory_1.sellPrice,
                buyPrice = inventory_1.buyPrice,
                date = inventory_1.date,
                saleNumber = sellNumber.toString(),
                createdTimeStamp = inventory_1.createdTimeStamp,
                remainingInventory = inventory_1.number.toLong(),
                transaction = TransactionState.Sale.state
            )
            saleInventory(
                inventoryEntity = inventory_1,
                history = history_1
            )
            val inventories_1 = getInventory().first()
            val histories = getHistory().first()
            var inventoryTest = false
            for (i in inventories_1){
                if(i.createdTimeStamp == inventory_1.createdTimeStamp &&
                    i.number == (inventory.number.toLong() - sellNumber).toString()
                ){
                    inventoryTest = true
                }
            }
            assert(inventoryTest)
            var historyTest = false
            for (i in histories){
                if(i.createdTimeStamp == history_1.createdTimeStamp &&
                    i.remainingInventory.toString() == (inventory.number.toLong() - sellNumber).toString() &&
                    i.transaction == TransactionState.Sale.state
                ){
                    historyTest = true
                }
            }
            assert(historyTest)
            // test update sale history
            if(historyTest && inventoryTest){
                val newSaleNumber = 4
                val preSaleUpdateHistories = getHistory().first()
                val preSaleUpdateHistory = preSaleUpdateHistories.first{
                    it.createdTimeStamp == history_1.createdTimeStamp &&
                            it.transaction == history_1.transaction &&
                            it.timeStamp == history_1.timeStamp
                }
                val preSaleUpdateInventory = inventory_1.copy(
                    timeStamp = System.currentTimeMillis(),
                    number = (inventory_1.number.toLong() - newSaleNumber.toLong()).toString(),
                )
                updateSaleHistory(
                    inventoryEntity = preSaleUpdateInventory,
                    oldHistory = history_1,
                    newHistory = preSaleUpdateHistory.copy(
                        saleNumber = newSaleNumber.toString(),
                        remainingInventory = inventory_1.number.toLong() - newSaleNumber,
                        timeStamp = preSaleUpdateInventory.timeStamp
                    )
                )
                val postUpdateHistories = getHistory().first()
                val postUpdateInventories = getInventory().first()
                var postUpdateTestHistory = false
                var postUpdateTestInventory = false
                for(i in postUpdateInventories){
                    if(i.createdTimeStamp == preSaleUpdateInventory.createdTimeStamp
                        && i.number == (inventory_1.number.toLong() - newSaleNumber.toLong() +
                                history_1.saleNumber.toLong()).toString()
                    ){
                        postUpdateTestInventory = true
                    }
                }
                print(inventory_1.number.toLong() - newSaleNumber.toLong())
                assert(postUpdateTestInventory)
                for(i in postUpdateHistories){
                    if(
                        i.createdTimeStamp == preSaleUpdateInventory.createdTimeStamp
                        && i.remainingInventory == inventory_1.number.toLong() - newSaleNumber.toLong()
                        && i.saleNumber == newSaleNumber.toString()
                    ){
                        postUpdateTestHistory = true
                    }
                }
                assert(postUpdateTestHistory)
            }else{
                assert(false)
            }
        }else{
            assert(false)
        }
    }
    // number field unit test
    @Test
    fun throwAnExceptionForEmptyNumber() = runBlocking{
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
        // test sale inventory
        if(insertTest) {
            try {
                val sellNumber = ""
                val newInventory = inventory.copy(
                    timeStamp = System.currentTimeMillis(),
                    number = (inventory.number.toLong() - sellNumber.toLong()).toString()
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
                try {
                    saleInventory(
                        inventoryEntity = newInventory,
                        history = newHistory
                    )
                    assert(false)
                } catch (e: InvalidTransactionException) {
                    assert(true)
                }
            }catch (e: java.lang.NumberFormatException){
                e.printStackTrace()
            }
        }
    }
    @Test
    fun throwAnExceptionForNegativeNumber() = runBlocking{
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
        // test sale inventory
        if(insertTest) {
            try {
                val sellNumber = -4
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
                try {
                    saleInventory(
                        inventoryEntity = newInventory,
                        history = newHistory
                    )
                    assert(false)
                } catch (e: InvalidTransactionException) {
                    assert(true)
                }
            }catch (e: java.lang.NumberFormatException){
                e.printStackTrace()
            }
        }
    }
    @Test
    fun throwAnExceptionForNumberIsNotDigit() = runBlocking{
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
        // test sale inventory
        if(insertTest) {
            try {
                val sellNumber = "D"
                val newInventory = inventory.copy(
                    timeStamp = System.currentTimeMillis(),
                    number = (inventory.number.toLong() - sellNumber.toLong()).toString()
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
                try {
                    saleInventory(
                        inventoryEntity = newInventory,
                        history = newHistory
                    )
                    assert(false)
                } catch (e: InvalidTransactionException) {
                    assert(true)
                }
            }catch (e: java.lang.NumberFormatException){
                e.printStackTrace()
            }
        }
    }
    @Test
    fun throwAnExceptionForZeroNumber() = runBlocking{
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
        // test sale inventory
        if(insertTest) {
            try {
                val sellNumber = 0
                val newInventory = inventory.copy(
                    timeStamp = System.currentTimeMillis(),
                    number = (inventory.number.toLong() - sellNumber.toLong()).toString()
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
                try {
                    saleInventory(
                        inventoryEntity = newInventory,
                        history = newHistory
                    )
                    assert(false)
                } catch (e: InvalidTransactionException) {
                    assert(true)
                }
            }catch (e: java.lang.NumberFormatException){
                e.printStackTrace()
            }
        }
    }
    @Test
    fun throwAnExceptionForMoreThanTheNumberOfAvailableItems() = runBlocking{
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
        // test sale inventory
        if(insertTest) {
            try {
                val sellNumber = 80
                val newInventory = inventory.copy(
                    timeStamp = System.currentTimeMillis(),
                    number = (inventory.number.toLong() - sellNumber.toLong()).toString()
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
                try {
                    saleInventory(
                        inventoryEntity = newInventory,
                        history = newHistory
                    )
                    assert(false)
                } catch (e: InvalidTransactionException) {
                    assert(true)
                }
            }catch (e: java.lang.NumberFormatException){
                e.printStackTrace()
            }
        }
    }
}