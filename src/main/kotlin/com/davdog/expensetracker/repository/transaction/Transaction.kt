package com.davdog.expensetracker.repository.transaction

import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.time.LocalDate

import org.springframework.data.annotation.Id;
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class Transaction (@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                   val transactionDate: LocalDate,
                   val amount: BigDecimal,
                   val type: String,
                   val description: String,
                   @Id val id: String = UUID.randomUUID().toString(),
                   var expenseType: ExpenseType? = null)
