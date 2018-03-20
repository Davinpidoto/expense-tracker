package com.davdog.expensetracker.controller

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.service.ExpenseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
class ExpenseController(val expenseService: ExpenseService) {

  @GetMapping("/expenses")
  fun getTenantsReceipts(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>) : List<Expense> {
    return expenseService.getExpenses(from, to)
  }

  @PostMapping("/csv")
  fun handleFileUpload(@RequestParam("file") file: MultipartFile): List<Expense> {
    return expenseService.saveTransactions(file)
  }

}
