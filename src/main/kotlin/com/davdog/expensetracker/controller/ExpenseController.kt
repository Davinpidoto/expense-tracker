package com.davdog.expensetracker.controller

import com.davdog.expensetracker.controller.json.ExpenseResponse
import com.davdog.expensetracker.controller.json.StatsResponse
import com.davdog.expensetracker.controller.json.UpdateExpenseRequest
import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.service.ExpenseService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@CrossOrigin(origins = ["http://localhost:8081"])
class ExpenseController(val expenseService: ExpenseService) {

  @GetMapping("/expenses")
  fun getTenantsReceipts(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>) : List<ExpenseResponse> {
    return expenseService.getExpenses(from, to)
  }

  @PatchMapping("/expenses/{id}")
  fun updateExpense(@PathVariable id: String, @Valid @RequestBody request: UpdateExpenseRequest): Expense {
    return expenseService.updateExpense(id, request)
  }



  @PostMapping("/csv")
  fun handleFileUpload(@RequestParam("file") file: MultipartFile): List<Expense> {
    return expenseService.saveTransactions(file)
  }



}
