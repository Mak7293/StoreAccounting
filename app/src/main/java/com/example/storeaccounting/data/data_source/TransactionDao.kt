package com.example.storeaccounting.data.data_source

import android.content.LocusId
import androidx.room.*
import com.example.storeaccounting.domain.model.Transaction
import com.example.storeaccounting.domain.util.TransactionState
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // update existing note when insert note with existing id
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("SELECT * FROM transaction_table")
    fun fetchAllData(): Flow<List<Transaction>>

    @Query("SELECT * FROM transaction_table WHERE transactionState = :inventory")
    fun fetchAllInventoryTransaction(
        inventory: String = TransactionState.Inventory.state):Flow<List<Transaction>>
}