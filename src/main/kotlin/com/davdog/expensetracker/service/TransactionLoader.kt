package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class TransactionLoader(val expenseTypeRepository: ExpenseTypeRepository) {

  val dateIndex = 0
  val amountIndex = 1
  val typeIndex = 4
  val descriptionIndex = 5


  fun loadTransactions(multipart: MultipartFile) : MutableList<Expense> {
    val defaultExpenseType = expenseTypeRepository.findByType("NA")
    val transactions = ArrayList<Expense>()
    val records = CSVFormat.RFC4180.parse(InputStreamReader(multipart.inputStream))

    records.forEach{
        transactions.add(Expense(extractDate(it[dateIndex]),
            formatAmount(it[amountIndex]), it[typeIndex], formatDescription(it[descriptionIndex]), defaultExpenseType))
    }

    return transactions
  }

  private fun extractDate(dateString: String) : LocalDate {
    val pattern : String = if (dateString.contains('-'))  "dd-MMM-yy" else "dd MMM yy"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
    return LocalDate.parse(dateString, formatter)
  }

  private fun formatAmount(amount: String) : BigDecimal {
      return BigDecimal(amount.replace(",","")).multiply(BigDecimal(-1))
  }

  private fun formatDescription(description: String): String {
    return description.replace("  ","")
  }

}
