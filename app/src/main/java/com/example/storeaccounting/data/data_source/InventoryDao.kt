package com.example.storeaccounting.data.data_source

import androidx.room.*
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity
import com.example.storeaccounting.domain.model.InventoryWithHistory
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

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

    @Query("SELECT * FROM inventory_entity_table ORDER BY createdTimeStamp DESC")
    fun fetchAllInventory(): Flow<List<InventoryEntity>>

    @Query("SELECT * FROM history_table ORDER BY createdTimeStamp DESC ")
    fun fetchAllHistory(): Flow<List<History>>

    @Transaction
    @Query("SELECT * FROM inventory_entity_table WHERE createdTimestamp = :createdTimestamp")
    suspend fun getHistoriesByInventoryTimeStamp(createdTimestamp: Long): InventoryWithHistory


}