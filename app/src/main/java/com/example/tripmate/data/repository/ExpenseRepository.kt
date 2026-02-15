package com.example.tripmate.data.repository

import com.example.tripmate.data.local.ExpenseDao
import com.example.tripmate.data.model.ExpenseEntity

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getExpensesByTrip(tripId: Long) =
        expenseDao.getExpensesByTrip(tripId)

    suspend fun insert(expense: ExpenseEntity) =
        expenseDao.insertExpense(expense)

    suspend fun update(expense: ExpenseEntity) =
        expenseDao.updateExpense(expense)

    suspend fun delete(expense: ExpenseEntity) =
        expenseDao.deleteExpense(expense)
}