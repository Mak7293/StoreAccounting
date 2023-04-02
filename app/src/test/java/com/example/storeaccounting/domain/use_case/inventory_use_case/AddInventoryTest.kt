package com.example.storeaccounting.domain.use_case.inventory_use_case

import com.example.storeaccounting.data.repository.FakeRepository
import com.example.storeaccounting.domain.custom_exception.InvalidTransactionException
import com.example.storeaccounting.domain.model.InventoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class AddInventoryTest{

    private lateinit var addInventory: AddInventory
    private lateinit var getInventory: GetInventory
    private lateinit var fakeRepository: FakeRepository
    private lateinit var getHistory: GetHistory

    @Before
    fun setup(){
        fakeRepository = FakeRepository()
        getInventory = GetInventory(fakeRepository)
        addInventory = AddInventory(fakeRepository)
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
    fun insertInventory() = runBlocking{
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
        if(insertTest){
            insertTest = false
            val histories = getHistory().first()
            for(i in histories){
                if (i.createdTimeStamp == inventory.createdTimeStamp){
                    insertTest = true
                }
            }
            assert(insertTest)
        }else{
            assert(false)
        }
    }
    // title field unit test
    @Test
    fun throwAnExceptionForEmptyTitle() = runBlocking{
        val string = "string"
        val inventory = InventoryEntity(
            title = "",
            date = string,
            timeStamp = System.currentTimeMillis(),
            createdTimeStamp = System.currentTimeMillis(),
            buyPrice =(string.length*(10..15).random()).toString(),
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(5..10).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            number = ""
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            number = "-3"
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            number = "A"
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            number = "0"
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice ="",
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice ="-3",
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice ="A",
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice = "0",
            sellPrice = (string.length*(15..20).random()).toString(),
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice = (string.length*(10..15).random()).toString(),
            sellPrice = "",
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice = (string.length*(10..15).random()).toString(),
            sellPrice = "-1",
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice = (string.length*(10..15).random()).toString(),
            sellPrice = "A",
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
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
            buyPrice = (string.length*(10..15).random()).toString(),
            sellPrice = "0",
            number = (string.length*(1..5).random()).toString()
        )
        try {
            addInventory(inventory)
            assert(false)
        }catch (e: InvalidTransactionException){
            assert(true)
        }
    }

}