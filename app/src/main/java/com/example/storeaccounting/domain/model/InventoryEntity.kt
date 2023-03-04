package com.example.storeaccounting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_entity_table")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String = "",
    val timeStamp: Long = 0,
    val title: String = "",
    val number: String = "",
    val sellPrice: String = "",
    val buyPrice: String = "",
)