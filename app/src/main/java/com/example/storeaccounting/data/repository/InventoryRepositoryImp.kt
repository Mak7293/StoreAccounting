package com.example.storeaccounting.data.repository

import com.example.storeaccounting.data.data_source.InventoryDao
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class InventoryRepositoryImp(private val dao: InventoryDao): InventoryRepository {

    override suspend fun insertInventory(inventoryEntity: InventoryEntity) {
        dao.insertInventory(inventoryEntity)
    }

    override suspend fun deleteInventory(inventoryEntity: InventoryEntity) {
        dao.deleteInventory(inventoryEntity)
    }

    override suspend fun updateInventory(inventoryEntity: InventoryEntity) {
        dao.updateInventory(inventoryEntity)
    }

    override fun fetchAllInventory(): Flow<List<InventoryEntity>> {
        return dao.fetchAllInventory()
    }

    override suspend fun getHistoriesByInventoryTimeStamp(timeStamp: Long): InventoryWithHistory {
        return dao.getHistoriesByInventoryTimeStamp(timeStamp)
    }

    override suspend fun insertHistory(history: History) {
        dao.insertHistory(history)
    }

    override suspend fun deleteHistory(history: History) {
        dao.deleteHistory(history)
    }

    override suspend fun updateHistory(history: History) {
        dao.updateHistory(history)
    }

    override fun fetchAllHistory(): Flow<List<History>> {
        return dao.fetchAllHistory()
    }
}