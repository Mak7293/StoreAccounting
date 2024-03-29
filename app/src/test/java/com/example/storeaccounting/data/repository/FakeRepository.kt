package com.example.storeaccounting.data.repository

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository:Repository {

    private val inventoryList = mutableListOf<InventoryEntity>()
    private val historyList = mutableListOf<History>()
    private val creditCardList = mutableListOf<CreditCard>()

    override suspend fun insertInventory(inventoryEntity: InventoryEntity) {
        inventoryList.add(inventoryEntity)
    }
    override suspend fun deleteInventory(inventoryEntity: InventoryEntity) {
        inventoryList.remove(inventoryEntity)
    }
    override suspend fun updateInventory(inventoryEntity: InventoryEntity) {
        val oldInventory = inventoryList.filter {
            it.createdTimeStamp == inventoryEntity.createdTimeStamp
        }.first()
        inventoryList.remove(oldInventory)
        inventoryList.add(inventoryEntity)
    }
    override fun fetchAllInventory(): Flow<List<InventoryEntity>> {
        return flow {
            emit(inventoryList)
        }
    }
    override suspend fun getHistoriesByInventoryTimeStamp(createdTimeStamp: Long): InventoryWithHistory {
        val inventory = inventoryList.filter {
            it.createdTimeStamp == createdTimeStamp
        }.last()
        val history = historyList.filter {
            it.createdTimeStamp == createdTimeStamp
        }
        return InventoryWithHistory(
            inventory = inventory,
            history = history

        )
    }
    override suspend fun insertHistory(history: History) {
        historyList.add(history)
    }
    override suspend fun deleteHistory(history: History) {
        historyList.remove(history)
    }
    override suspend fun updateHistory(history: History) {
        val oldHistory = historyList.filter {
            it.id == history.id
        }.first()
        historyList.remove(oldHistory)
        historyList.add(history)
    }
    override fun fetchAllHistory(): Flow<List<History>> {
        return flow{
            emit(historyList)
        }
    }
    override suspend fun createCreditCard(creditCard: CreditCard) {
        creditCardList.add(creditCard)
    }
    override suspend fun deleteCreditCard(creditCard: CreditCard) {
        creditCardList.remove(creditCard)
    }
    override suspend fun updateCreditCard(creditCard: CreditCard) {
        creditCardList.filter {
            it.id == creditCard.id
        }.last().copy(
            id = creditCard.id,
            cardNumber = creditCard.cardNumber,
            userName = creditCard.userName,
            irShaba = creditCard.irShaba,
            expireDate = creditCard.expireDate,
            description = creditCard.description,
            cvv2 = creditCard.cvv2,
            bankName = creditCard.bankName
        )
    }
    override fun fetchAllCreditCard(): Flow<List<CreditCard>> {
        return flow{
            emit(creditCardList)
        }
    }
    override suspend fun fetchSpecificCreditCard(id: Int): CreditCard {
        return creditCardList.filter {
            it.id == id
        }.last()
    }

}