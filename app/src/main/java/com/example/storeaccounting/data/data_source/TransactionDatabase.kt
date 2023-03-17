package com.example.storeaccounting.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storeaccounting.domain.model.CreditCard
import com.example.storeaccounting.domain.model.History
import com.example.storeaccounting.domain.model.InventoryEntity


@Database(
    entities = [InventoryEntity::class,History::class,CreditCard::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase: RoomDatabase() {
    abstract val transactionDao: InventoryDao
    abstract val creditCardDao: CreditCardDao

    companion object {
        const val DATABASE_NAME = "transaction_db"
    }
}