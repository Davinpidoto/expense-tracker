package com.davdog.expensetracker.service

import com.davdog.expensetracker.config.DebitTypes
import com.davdog.expensetracker.controller.json.ExpenseResponse
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
                     val expenseTypeRepository: ExpenseTypeRepository,
                     val debitTypes: DebitTypes) {

  lateinit var typeMap: Map<String, ExpenseType>

  @PostConstruct
  fun init() {
    typeMap = createMap()
  }

  fun saveTransactions(file: MultipartFile): List<Expense> {
    val expenses = transactionLoader.loadTransactions(file)
    removeInvalidExpenses(expenses)
    assignExpenseType(expenses)
    removeDuplicateExpenses(expenses)
    return expenseRepository.saveAll(expenses)
  }

  fun getExpenses(from: Optional<String>, to: Optional<String>): List<ExpenseResponse> {
    val expenses = expenseRepository.getExpenses(from, to)
    return expenses.map{
      ExpenseResponse(it.transactionDate, formatAmount(it.amount), it.description, it.expenseType.name, it.id)
    }
  }

  fun updateExpense(expenseId: String, request: UpdateExpenseRequest): Expense {
    val expense = expenseRepository.findById(expenseId).get()
    val expenseType = expenseTypeRepository.findByName(request.expenseType)
    return expenseRepository.save(Expense(expense.transactionDate, expense.amount, expense.type, expense.description, expenseType, expense.id))
  }

  private fun removeInvalidExpenses(expenses: MutableList<Expense>) {
    expenses.removeIf{!debitTypes.types.contains(it.type)}
    expenses.removeIf{it.type == "TRANSFER DEBIT" && it.description.contains("Linked Acc Trns")}
    expenses.removeIf{it.type == "INTER-BANK CREDIT" && !it.description.contains("MCARE")}
  }

  private fun assignExpenseType(expenses: MutableList<Expense>) {
    val expenseTypes = expenseTypeRepository.findAll()

    expenses.forEach { expense ->
      expenseTypes.forEach { expenseType ->
        expenseType.identifiers?.forEach {
          if (expense.description.toUpperCase().contains(it.toUpperCase())) {
            expense.expenseType = expenseType
          }
        }
      }
    }
  }

  private fun removeDuplicateExpenses(expenses: MutableList<Expense>) {
    expenses.removeIf{expenseRepository.findByTransactionDateAndAmountAndDescriptionAndType(
            it.transactionDate, it.amount, it.description, it.type) != null}
  }

  private fun formatAmount(amount: BigDecimal) : String {
    return "$${amount}"
  }

  private fun createMap(): Map<String, ExpenseType> {
    val typeMap: MutableMap<String, ExpenseType> = HashMap()
    val expenseTypes = expenseTypeRepository.findAll()
    expenseTypes.forEach{typeMap[it.name]= it}
    return typeMap
  }

}
