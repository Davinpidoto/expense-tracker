package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.transaction.Transaction
import org.apache.commons.csv.CSVFormat
import java.io.FileReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import org.springframework.core.io.ClassPathResource

class TransactionLoader {

    val dateIndex = 0
    val amountIndex = 1
    val typeIndex = 4
    val descriptionIndex = 5

    fun loadTransactions(file: String) : List<Transaction> {
        val transactions = ArrayList<Transaction>()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH)
        val resource = ClassPathResource(file)
        val records = CSVFormat.RFC4180.parse(FileReader(resource.file))

        records.forEach{
            transactions.add(Transaction(LocalDate.parse(it[dateIndex], formatter),
                extractAmount(it[amountIndex]), it[typeIndex], it[descriptionIndex]))
        }

        return transactions
    }

    private fun extractAmount(amount: String) : BigDecimal {
        return BigDecimal(amount.replace(",",""))
    }

}
