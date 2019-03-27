package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import org.apache.commons.csv.CSVFormat
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
@ConfigurationProperties(prefix = "csv")
class TransactionLoader(val expenseTypeRepository: ExpenseTypeRepository) {

  lateinit var dateIndex: String
  lateinit var amountIndex: String
  lateinit var typeIndex: String
  lateinit var descriptionIndex: String

  fun loadTransactions(multipart: MultipartFile) : MutableList<Expense> {
    val defaultExpenseType = expenseTypeRepository.findByName("NA")
    val transactions = ArrayList<Expense>()
    val records = CSVFormat.RFC4180.parse(InputStreamReader(multipart.inputStream))

    records.forEach{
        transactions.add(Expense(extractDate(it[dateIndex.toInt()]),
            formatAmount(it[amountIndex.toInt()]), it[typeIndex.toInt()], formatDescription(it[descriptionIndex.toInt()]), defaultExpenseType))
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
