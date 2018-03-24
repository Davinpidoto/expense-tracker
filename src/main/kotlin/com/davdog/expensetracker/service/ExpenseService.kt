package com.davdog.expensetracker.service

import com.davdog.expensetracker.controller.json.UpdateExpenseRequest
import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expense.ExpenseRepository
import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap

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
    val defaultExpenseType = expenseTypeRepository.findByType("NA")
    expenses.removeIf{!debitTypes.contains(it.type)}
    expenses.forEach { expense ->
      expenseTypes.forEach { expenseType ->
        expenseType.identifiers?.forEach {
          if (expense.description.toUpperCase().contains(it.toUpperCase())) {
            expense.expenseType = expenseType
          }
        }
      }
      expense.expenseType = expense.expenseType?: defaultExpenseType
    }
    return expenseRepository.save(expenses)
  }


  fun getExpenses(from: Optional<String>, to: Optional<String>): List<Expense> {
    return expenseRepository.getExpenses(from, to)
  }

  fun updateExpense(expenseId: String, request: UpdateExpenseRequest): Expense {
    val expense = expenseRepository.findOne(expenseId)
    val expenseType = expenseTypeRepository.findOne(request.expenseTypeId)
    return expenseRepository.save(Expense(expense.transactionDate, expense.amount, expense.type, expense.description, expense.id, expenseType))
  }

  fun getStats(from: Optional<String>, to: Optional<String>): MutableMap<String?, BigDecimal> {
    val expenses = expenseRepository.getExpenses(from, to)
    val groups = expenses.groupBy({ it.expenseType?.type }, {it})
    val map : MutableMap<String?, BigDecimal> = HashMap()
      groups.forEach{k, v -> map[k ?: "NA"] = v.map{it.amount}.reduce{a, b -> a.plus(b)}.abs()
    }
    return map
  }

  fun getStatsForCsv(from: Optional<String>, to: Optional<String>): ArrayList<String>{
    val rows = ArrayList<String>()
    rows.add("Category,Amount")
    rows.add("\n")

    getStats(from, to).forEach{
      rows.add("${it.key},${it.value}")
      rows.add("\n")
    }
    return rows
  }


  fun createMap(): Map<String, ExpenseType> {
    val typeMap: MutableMap<String, ExpenseType> = HashMap()
    val expenseTypes = expenseTypeRepository.findAll()
    expenseTypes.forEach{typeMap.put(it.type, it)}
    return typeMap
  }

}
