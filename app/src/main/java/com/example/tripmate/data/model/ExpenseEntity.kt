package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Long,

    val title: String,
    val amount: Double,
    val date: String,
    val notes: String,
    val category: String,
    val paidBy: String
)