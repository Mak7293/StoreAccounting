package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.data.repository.FakeRepository
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.util.TransactionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateInventoryTest{
    private lateinit var addInventory: AddInventory
    private lateinit var updateInventory: UpdateInventory
    private lateinit var getInventory: GetInventory
    private lateinit var getHistory: GetHistory
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        getInventory = GetInventory(fakeRepository)
        addInventory = AddInventory(fakeRepository)
        getHistory = GetHistory(fakeRepository)
        updateInventory = UpdateInventory(fakeRepository)

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
    fun updateInventory() = runBlocking{
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
            val newTitle = "updated_inventory"
            val updatedInventory = inventories.filter {
                it.createdTimeStamp == inventory.createdTimeStamp
            }.first()
            updateInventory(updatedInventory.copy(title= newTitle))
            val newList = getInventory().first()
            var updateTest: Boolean = false
            val newInventory = newList.filter {
                it.createdTimeStamp == inventory.createdTimeStamp
            }.first()
            if (newInventory.title == newTitle){
                updateTest = true
            }
            assert(updateTest)
            // check whether history with TransactionState = Edit exist.
            var historyTest = false
            val histories = getHistory().first()
            for(i in histories){
                if(i.createdTimeStamp == inventory.createdTimeStamp &&
                    i.title == newTitle &&
                    i.transaction == TransactionState.Edit.state
                ){
                    historyTest = true
                }
            }
            assert(historyTest)
        }else{
            assert(false)
        }
    }
    // title field unit test
    @Test
    fun throwAnExceptionForEmptyTitle() = runBlocking{
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
        // test throwing exception for empty title
        if(insertTest){
            val newInventory = inventory.copy(title = "")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
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
            // test throwing exception for empty number
            if(insertTest){
                val newInventory = inventory.copy(number = "")
                try {
                    updateInventory(newInventory)
                    assert(false)
                }catch (e: InvalidTransactionException){
                    assert(true)
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
        // test throwing exception for negative number
        if(insertTest){
            val newInventory = inventory.copy(number = "-5")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
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
        // test throwing exception for number is not digit
        if(insertTest){
            val newInventory = inventory.copy(number = "H")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
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
        // test throwing exception for zero number
        if(insertTest){
            val newInventory = inventory.copy(number = "0")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    // buy price field unit test
    @Test
    fun throwAnExceptionForEmptyBuyPrice() = runBlocking{
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
        // test throwing exception for empty buy price
        if(insertTest){
            val newInventory = inventory.copy(buyPrice = "")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForNegativeBuyPrice() = runBlocking{
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
        // test throwing exception for negative buy price
        if(insertTest){
            val newInventory = inventory.copy(buyPrice = "-6")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForBuyPriceIsNotDigit() = runBlocking{
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
        // test throwing exception for buy price is not digit
        if(insertTest){
            val newInventory = inventory.copy(buyPrice = "T")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForZeroBuyPrice() = runBlocking{
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
        // test throwing exception for zero buy price
        if(insertTest){
            val newInventory = inventory.copy(buyPrice = "0")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    // sell price field unit test
    @Test
    fun throwAnExceptionForEmptySellPrice() = runBlocking{
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
        // test throwing exception for empty sell price
        if(insertTest){
            val newInventory = inventory.copy(sellPrice = "")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForNegativeSellPrice() = runBlocking{
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
        // test throwing exception for negative sell price
        if(insertTest){
            val newInventory = inventory.copy(sellPrice = "-3")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForSellPriceIsNotDigit() = runBlocking{
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
        // test throwing exception for sellPrice is not digit
        if(insertTest){
            val newInventory = inventory.copy(number = "R")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
    @Test
    fun throwAnExceptionForZeroSellPrice() = runBlocking{
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
        // test throwing exception for zero sell price
        if(insertTest){
            val newInventory = inventory.copy(sellPrice = "0")
            try {
                updateInventory(newInventory)
                assert(false)
            }catch (e: InvalidTransactionException){
                assert(true)
            }
        }
    }
}