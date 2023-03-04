package com.example.storeaccounting.data.data_source

import androidx.room.*
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // update existing note when insert note with existing id
    suspend fun insertInventory(inventoryEntity: InventoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE) // update existing note when insert note with existing id
    suspend fun insertHistory(history: History)

    @Delete
    suspend fun deleteInventory(inventoryEntity: InventoryEntity)

    @Delete
    suspend fun deleteHistory(history: History)

    @Update
    suspend fun updateInventory(inventoryEntity: InventoryEntity)

    @Query("SELECT * FROM inventory_entity_table")
    fun fetchAllInventory(): Flow<List<InventoryEntity>>

    @Transaction
    @Query("SELECT * FROM inventory_entity_table WHERE id = :id")
    suspend fun getHistoriesByInventoryTimeStamp(id: Int): InventoryWithHistory


}