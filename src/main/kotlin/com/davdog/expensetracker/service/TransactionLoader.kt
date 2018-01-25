package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expense.Expense
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class TransactionLoader {

    val dateIndex = 0
    val amountIndex = 1
    val typeIndex = 4
    val descriptionIndex = 5

    fun loadTransactions(multipart: MultipartFile) : MutableList<Expense> {
      val transactions = ArrayList<Expense>()
      val formatter = DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH)
      val records = CSVFormat.RFC4180.parse(InputStreamReader(multipart.inputStream))

      records.forEach{
          transactions.add(Expense(LocalDate.parse(it[dateIndex], formatter),
              extractAmount(it[amountIndex]), it[typeIndex], it[descriptionIndex]))
      }

      return transactions
    }

    private fun extractAmount(amount: String) : BigDecimal {
        return BigDecimal(amount.replace(",",""))
    }

}
