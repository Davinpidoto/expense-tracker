package com.davdog.expensetracker.service

import com.davdog.expensetracker.controller.json.ExpenseResponse
import com.davdog.expensetracker.controller.json.StatsResponse
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
    expenses.removeIf { expenseRepository.findByTransactionDateAndAmountAndDescriptionAndType(it.transactionDate, it.amount, it.description, it.type) != null }
    return expenseRepository.save(expenses)
  }


  fun getExpenses(from: Optional<String>, to: Optional<String>): List<ExpenseResponse> {
    val expenses = expenseRepository.getExpenses(from, to)
    return expenses.map { ExpenseResponse(it.transactionDate, formatAmount(it.amount), it.description, it.expenseType.type, it.id) }
  }

  fun updateExpense(expenseId: String, request: UpdateExpenseRequest): Expense {
    val expense = expenseRepository.findOne(expenseId)
    val expenseType = expenseTypeRepository.findByType(request.expenseType)
    return expenseRepository.save(Expense(expense.transactionDate, expense.amount, expense.type, expense.description, expenseType, expense.id))
  }

  fun getRawStats(from: Optional<String>, to: Optional<String>): Map<String, BigDecimal> {
    val expenses = expenseRepository.getExpenses(from, to)
    val map : MutableMap<String, BigDecimal> = HashMap()
    val groups = expenses.groupBy({ it.expenseType.type }, {it})
    groups.forEach{k, v -> map[k] = v.map{it.amount}.reduce{a, b -> a.plus(b)} }
    return map
  }

  fun getStats(from: Optional<String>, to: Optional<String>): StatsResponse {
    val formattedResponse : MutableMap<String, String> = HashMap()
    getRawStats(from, to).forEach{formattedResponse[it.key]=formatAmount(it.value)}
    return StatsResponse(from.orElse(""), to.orElse(""), formattedResponse)
  }

  fun getStatsForCsv(from: Optional<String>, to: Optional<String>): ArrayList<String> {
    val rows = ArrayList<String>()
    rows.add("Category,Amount")
    rows.add("\n")

    getRawStats(from, to).forEach{
      rows.add("${it.key},${it.value}")
      rows.add("\n")
    }
    return rows
  }

  fun formatAmount(amount: BigDecimal) : String {
    return "$${amount}"
  }


  fun createMap(): Map<String, ExpenseType> {
    val typeMap: MutableMap<String, ExpenseType> = HashMap()
    val expenseTypes = expenseTypeRepository.findAll()
    expenseTypes.forEach{typeMap[it.type]= it}
    return typeMap
  }

}
