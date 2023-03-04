package com.example.storeaccounting.domain.model

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.storeaccounting.domain.util.TransactionState

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val transaction: String,
    val title: String?,
    val number: String?,
    val buyPrice: String?,
    val sellPrice: String?,
    val date: String?,
    val timeStamp: Long?,
)
