package com.example.storeaccounting.domain.repository

import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun insertInventory(inventoryEntity: InventoryEntity)
    suspend fun deleteInventory(inventoryEntity: InventoryEntity)
    suspend fun updateInventory(inventoryEntity: InventoryEntity)
    fun fetchAllInventory(): Flow<List<InventoryEntity>>
    suspend fun getHistoriesByInventoryTimeStamp(createdTimeStamp: Long): InventoryWithHistory
    suspend fun insertHistory(history: History)
    suspend fun deleteHistory(history: History)
    suspend fun updateHistory(history: History)
    fun fetchAllHistory(): Flow<List<History>>
    suspend fun createCreditCard(creditCard: CreditCard)
    suspend fun deleteCreditCard(creditCard: CreditCard)
    suspend fun updateCreditCard(creditCard: CreditCard)
    fun fetchAllCreditCard(): Flow<List<CreditCard>>
    suspend fun fetchSpecificCreditCard(id: Int): CreditCard
}