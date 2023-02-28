package com.example.storeaccounting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String,
    val timeStamp: Long,
    val title: String,
    val number: String,
    val sellPrice: String,
    val buyPrice: String,
    val state: String
)