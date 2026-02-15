package com.example.tripmate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tripmate.data.model.ExpenseEntity
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expense_table WHERE tripId = :tripId ORDER BY id DESC")
    fun getExpensesByTrip(tripId: Long): LiveData<List<ExpenseEntity>>

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)


}