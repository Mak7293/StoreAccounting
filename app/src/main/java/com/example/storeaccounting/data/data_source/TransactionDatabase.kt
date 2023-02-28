package com.example.storeaccounting.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storeaccounting.domain.model.Transaction


@Database(
    entities = [Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase: RoomDatabase() {
    abstract val transactionDao: TransactionDao

    companion object {
        const val DATABASE_NAME = "transaction_db"
    }
}