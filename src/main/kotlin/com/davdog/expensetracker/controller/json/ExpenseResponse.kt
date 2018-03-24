package com.davdog.expensetracker.controller.json

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
class ExpenseResponse(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                      val transactionDate: LocalDate,
                      val amount: String,
                      val description: String,
                      var expenseType: String,
                      val id: String)
