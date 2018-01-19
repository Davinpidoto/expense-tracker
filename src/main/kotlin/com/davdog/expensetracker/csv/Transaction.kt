package com.davdog.expensetracker.csv

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction (val transactionDate: LocalDate, val amount: BigDecimal, val type: String, val description: String)
