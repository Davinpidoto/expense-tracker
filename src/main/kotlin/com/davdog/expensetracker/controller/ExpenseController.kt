package com.davdog.expensetracker.controller

import com.davdog.expensetracker.repository.transaction.Transaction
import com.davdog.expensetracker.repository.transaction.TransactionRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExpenseController(val transactionRespository: TransactionRepository) {

  @GetMapping("/transactions")
  fun getTenantsReceipts() : MutableList<Transaction> {
    return transactionRespository.findAll()
  }

}
