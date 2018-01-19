package com.davdog.expensetracker.csv

import org.apache.commons.csv.CSVFormat
import java.io.FileReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import org.springframework.core.io.ClassPathResource

class TransactionLoader {

    fun loadTransactions(file: String) : List<Transaction> {
        val transactions =  ArrayList<Transaction>()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH)
        val resource = ClassPathResource(file)
        val records = CSVFormat.RFC4180.parse(FileReader(resource.file))

        records.forEach{
            transactions.add(Transaction(LocalDate.parse(it[0], formatter), extractAmount(it[1]), it[4], it[5]))
        }

        return transactions
    }

    private fun extractAmount(amount: String) : BigDecimal {
        return BigDecimal(amount.replace(",",""))
    }

}
