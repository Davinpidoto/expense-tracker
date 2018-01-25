package com.davdog.expensetracker.repository.expense

import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

import org.springframework.data.annotation.Id;
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class Expense(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                   val transactionDate: LocalDate,
              val amount: BigDecimal,
              val type: String,
              val description: String,
              @Id val id: String = UUID.randomUUID().toString(),
              var expenseType: ExpenseType? = null) {

  fun getExpenseType(): String {
    return if (expenseType != null) expenseType!!.type else "NA"
  }
}
