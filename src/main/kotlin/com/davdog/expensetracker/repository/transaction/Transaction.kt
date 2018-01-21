package com.davdog.expensetracker.repository.transaction

import com.davdog.expensetracker.repository.expensetype.ExpenseType
import java.math.BigDecimal
import java.time.LocalDate

import org.springframework.data.annotation.Id;
import java.util.*


class Transaction (val transactionDate: LocalDate,
                   val amount: BigDecimal,
                   val type: String,
                   val description: String,
                   @Id val id: String = UUID.randomUUID().toString(),
                   var expenseType: ExpenseType? = null)
