package com.example.storeaccounting.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class InventoryWithHistory (
    @Embedded
    val inventory: InventoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val history: List<History>
)