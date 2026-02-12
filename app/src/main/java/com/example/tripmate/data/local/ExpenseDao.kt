package com.example.tripmate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tripmate.data.model.ExpenseEntity

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expense_table WHERE tripId = :tripId")
    suspend fun getExpensesForTrip(tripId: Int): List<ExpenseEntity>
}