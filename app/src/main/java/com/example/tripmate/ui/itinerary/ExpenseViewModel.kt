package com.example.tripmate.ui.itinerary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tripmate.data.local.AppDatabase
import com.example.tripmate.data.model.ExpenseEntity
import com.example.tripmate.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: ExpenseRepository

    val expenses: LiveData<List<ExpenseEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(dao)

        expenses = repository.getExpensesByTrip(1L) // temporary hardcoded
    }

    fun insert(expense: ExpenseEntity) =
        viewModelScope.launch {
            repository.insert(expense)
        }

    fun update(expense: ExpenseEntity) =
        viewModelScope.launch {
            repository.update(expense)
        }

    fun delete(expense: ExpenseEntity) =
        viewModelScope.launch {
            repository.delete(expense)
        }
}