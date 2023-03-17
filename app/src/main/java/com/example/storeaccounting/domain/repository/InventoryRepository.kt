package com.example.storeaccounting.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {

    suspend fun insertInventory(inventoryEntity: InventoryEntity)
    suspend fun deleteInventory(inventoryEntity: InventoryEntity)
    suspend fun updateInventory(inventoryEntity: InventoryEntity)
    fun fetchAllInventory(): Flow<List<InventoryEntity>>
    suspend fun getHistoriesByInventoryTimeStamp(id: Long): InventoryWithHistory
    suspend fun insertHistory(history: History)
    suspend fun deleteHistory(history: History)
    suspend fun updateHistory(history: History)
    fun fetchAllHistory(): Flow<List<History>>
    suspend fun createCreditCard(creditCard: CreditCard)
    suspend fun deleteCreditCard(creditCard: CreditCard)
    suspend fun updateCreditCard(creditCard: CreditCard)
}