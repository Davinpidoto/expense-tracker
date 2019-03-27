package com.davdog.expensetracker.util

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expensetype.ExpenseType
import java.math.BigDecimal
import java.time.LocalDate

fun aTransaction() : TransactionBuilder {
  return TransactionBuilder()
}

class TransactionBuilder {

  private var transactionDate: LocalDate = LocalDate.now()
  private var amount = BigDecimal(100)
  private var transactionType = "MISCELLANEOUS DEBIT"
  private var description = "Telstra Phone Bill"
  private var expenseType = "Phone"


  fun build() : Expense {
    return Expense(transactionDate, amount, transactionType, description, ExpenseType(expenseType))
  }

  fun withTransactionDate(transactionDate: LocalDate) : TransactionBuilder {
    this.transactionDate = transactionDate
    return this
  }

  fun withAmount(amount: BigDecimal) : TransactionBuilder {
    this.amount = amount
    return this
  }

  fun withTransactionType(transactionType: String) : TransactionBuilder {
    this.transactionType = transactionType
    return this
  }

  fun withExpenseType(expenseType: String) : TransactionBuilder {
    this.expenseType = expenseType
    return this
  }

  fun withDescription(description: String) : TransactionBuilder {
    this.description = description
    return this
  }



}
