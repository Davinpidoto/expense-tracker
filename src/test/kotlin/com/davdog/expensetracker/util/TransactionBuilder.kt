package com.davdog.expensetracker.util

import com.davdog.expensetracker.repository.transaction.Transaction
import java.math.BigDecimal
import java.time.LocalDate

class TransactionBuilder {

  private var transactionDate: LocalDate = LocalDate.now()
  private var amount = BigDecimal(100)
  private var type = "MISCELLANEOUS DEBIT"
  private var description = "Telstra Phone Bill"


  fun build() : Transaction {
    return Transaction(transactionDate, amount, type, description)
  }

  fun withTransactionDate(transactionDate: LocalDate) : TransactionBuilder {
    this.transactionDate = transactionDate
    return this
  }

  fun withAmount(amount: BigDecimal) : TransactionBuilder {
    this.amount = amount
    return this
  }

  fun withType(type: String) : TransactionBuilder {
    this.type = type
    return this
  }

  fun withDescription(description: String) : TransactionBuilder {
    this.description = description
    return this
  }

  fun aTransaction() : TransactionBuilder {
    return TransactionBuilder()
  }

}
