package com.davdog.expensetracker.repository.expense

import java.util.*

interface ExpenseRepositoryCustom {

  fun getExpenses(from: Optional<String>, to: Optional<String>): List<Expense>

}
