package com.example.storeaccounting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Inventory")
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String,
    val timeStamp: Long,
    val title: String,
    val number: Int,
    val state: String
)