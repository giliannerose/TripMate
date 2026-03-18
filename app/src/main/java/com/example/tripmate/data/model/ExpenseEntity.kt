package com.example.tripmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(

    @PrimaryKey
    var id: Int = 0,

    var firestoreId: String = "",
    var userId: String = "",

    var tripId: String = "",

    var title: String = "",
    var amount: Double = 0.0,
    var date: String = "",
    var notes: String = "",
    var category: String = "",
    var paidBy: String = ""
)