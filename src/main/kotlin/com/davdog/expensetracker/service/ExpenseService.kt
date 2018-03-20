package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expense.ExpenseRepository
import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.annotation.PostConstruct

@Service
class ExpenseService(val transactionLoader: TransactionLoader,
                     val expenseRepository: ExpenseRepository,
                     val expenseTypeRepository: ExpenseTypeRepository) {

  val debitTypes: MutableList<String> = mutableListOf(
      "EFTPOS DEBIT",
      "AUTOMATIC DRAWING",
      "MISCELLANEOUS DEBIT",
      "ATM DEBIT",
      "TRANSFER DEBIT",
      "FEES",
      "CREDIT CARD PURCHASE",
      "PURCHASE AUTHORISATION")

  lateinit var typeMap: Map<String, ExpenseType>

  @PostConstruct
  fun init() {
    typeMap = createMap()
  }

  fun saveTransactions(file: MultipartFile): List<Expense> {
    val expenses = transactionLoader.loadTransactions(file)
    val expenseTypes = expenseTypeRepository.findAll()
    expenses.removeIf{!debitTypes.contains(it.type)}
    expenses.forEach { expense ->
      expenseTypes.forEach { expenseType ->
        expenseType.identifiers?.forEach {
          if (expense.description.toUpperCase().contains(it.toUpperCase())) {
            expense.expenseType = expenseType
          }
        }
      }
    }
    return expenseRepository.save(expenses)
  }


  fun getExpenses(from: Optional<String>, to: Optional<String>): List<Expense> {
    return expenseRepository.getExpenses(from, to)
  }

  fun createMap(): Map<String, ExpenseType> {
    val typeMap: MutableMap<String, ExpenseType> = HashMap()
    val expenseTypes = expenseTypeRepository.findAll()
    expenseTypes.forEach{typeMap.put(it.type, it)}
    return typeMap
  }

}
