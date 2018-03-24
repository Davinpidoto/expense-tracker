package com.davdog.expensetracker.repository.expense

import org.springframework.data.mongodb.repository.MongoRepository
import java.math.BigDecimal
import java.time.LocalDate

interface ExpenseRepository: MongoRepository<Expense, String>, ExpenseRepositoryCustom {

  fun findByTransactionDateAndAmountAndDescriptionAndType(transactionDate: LocalDate, amount: BigDecimal, description: String, type: String): Expense?
}
