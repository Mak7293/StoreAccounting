package com.example.storeaccounting.domain.repository

import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import com.example.storeaccounting.presentation.util.NavigationRoute
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {

    suspend fun insertInventory(inventoryEntity: InventoryEntity)

    suspend fun deleteInventory(inventoryEntity: InventoryEntity)

    suspend fun updateInventory(inventoryEntity: InventoryEntity)

    fun fetchAllInventory(): Flow<List<InventoryEntity>>

    suspend fun getHistoriesByInventoryTimeStamp(id: Int): InventoryWithHistory

    suspend fun insertHistory(history: History)

    suspend fun deleteHistory(history: History)
}