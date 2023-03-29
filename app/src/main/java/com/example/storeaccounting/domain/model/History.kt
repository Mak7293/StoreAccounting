package com.example.storeaccounting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val createdTimeStamp: Long,
    val remainingInventory: Long,
    val transaction: String,
    val title: String,
    val saleNumber: String,
    val buyPrice: String,
    val sellPrice: String,
    val date: String,
    val timeStamp: Long,
)
