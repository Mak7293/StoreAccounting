package com.example.storeaccounting.data.repository

import com.example.storeaccounting.data.data_source.CreditCardDao
import com.example.storeaccounting.data.data_source.InventoryDao
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImp(
    private val inventoryDao: InventoryDao,
    private val creditCardDao: CreditCardDao
    ): Repository {

    override suspend fun insertInventory(inventoryEntity: InventoryEntity) {
        inventoryDao.insertInventory(inventoryEntity)
    }
    override suspend fun deleteInventory(inventoryEntity: InventoryEntity) {
        inventoryDao.deleteInventory(inventoryEntity)
    }
    override suspend fun updateInventory(inventoryEntity: InventoryEntity) {
        inventoryDao.updateInventory(inventoryEntity)
    }
    override fun fetchAllInventory(): Flow<List<InventoryEntity>> {
        return inventoryDao.fetchAllInventory()
    }
    override suspend fun getHistoriesByInventoryTimeStamp(timeStamp: Long): InventoryWithHistory {
        return inventoryDao.getHistoriesByInventoryTimeStamp(timeStamp)
    }
    override suspend fun insertHistory(history: History) {
        inventoryDao.insertHistory(history)
    }
    override suspend fun deleteHistory(history: History) {
        inventoryDao.deleteHistory(history)
    }
    override suspend fun updateHistory(history: History) {
        inventoryDao.updateHistory(history)
    }
    override fun fetchAllHistory(): Flow<List<History>> {
        return inventoryDao.fetchAllHistory()
    }
    override suspend fun createCreditCard(creditCard: CreditCard) {
        creditCardDao.createCreditCard(creditCard)
    }
    override suspend fun deleteCreditCard(creditCard: CreditCard) {
        creditCardDao.deleteCreditCard(creditCard)
    }
    override suspend fun updateCreditCard(creditCard: CreditCard) {
        creditCardDao.updateCreditCard(creditCard)
    }
    override fun fetchAllCreditCard(): Flow<List<CreditCard>> {
        return creditCardDao.fetchAllCreditCard()
    }
    override suspend fun fetchSpecificCreditCard(id: Int): CreditCard {
        return creditCardDao.fetchSpecificCreditCard(id)
    }
}