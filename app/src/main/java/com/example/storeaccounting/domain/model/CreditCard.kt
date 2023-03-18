package com.example.storeaccounting.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_card_table")
data class CreditCard(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val bankName: String = "",
    val irShaba: String = "",
    val cardNumber: String = "",
    val userName: String = "",
    val expireDate: String = "",
    val cvv2: String = "",
    val description: String = ""
)