package com.davdog.expensetracker.controller

import com.davdog.expensetracker.controller.json.UpdateExpenseRequest
import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.service.ExpenseService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


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

  @PatchMapping("/expenses/{id}")
  fun updateExpense(@PathVariable id: String, @Valid @RequestBody request: UpdateExpenseRequest): Expense {
    return expenseService.updateExpense(id, request)
  }

  @GetMapping("/stats")
  fun getStats(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>)  :MutableMap<String?, BigDecimal> {
    return expenseService.getStats(from ,to)
  }

  @RequestMapping( "/csv")
  fun downloadCSV(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>, response: HttpServletResponse) {
    response.contentType = "text/csv"
    response.setHeader("Content-disposition", "attachment;filename=stats.csv")
    val iterator = expenseService.getStatsForCsv(from, to).iterator()
    while (iterator.hasNext()) {
      response.outputStream.print(iterator.next())
    }
    response.outputStream.flush()

  }

}
