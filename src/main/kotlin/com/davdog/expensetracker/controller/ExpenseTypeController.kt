package com.davdog.expensetracker.controller

import com.davdog.expensetracker.controller.json.ExpenseTypeRequest
import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.service.ExpenseTypeService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class ExpenseTypeController(val service: ExpenseTypeService) {

  @GetMapping("/types")
  fun getTypes(): List<ExpenseType> {
    return service.findAll()
  }

  @PostMapping("/types")
  fun addType(@Valid @RequestBody request: ExpenseTypeRequest): ExpenseType {
    return service.save(request)
  }

  @PutMapping("/types/{id}")
  fun deleteType(@Valid @PathVariable id: String, @RequestBody request: ExpenseTypeRequest): ExpenseType {
    return service.update(id, request)
  }

  @DeleteMapping("/types/{id}")
  fun deleteType(@PathVariable id: String) {
    service.delete(id)
  }

}
