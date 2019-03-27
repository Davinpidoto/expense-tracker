package com.davdog.expensetracker.service

import com.davdog.expensetracker.controller.json.ExpenseTypeRequest
import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import org.springframework.stereotype.Service

@Service
class ExpenseTypeService(private val expenseTypeRepository: ExpenseTypeRepository) {

  fun save(expenseTypeRequest: ExpenseTypeRequest): ExpenseType {
    return expenseTypeRepository.save(ExpenseType(expenseTypeRequest.name, expenseTypeRequest.identifiers))
  }

  fun update(id: String, expenseTypeRequest: ExpenseTypeRequest): ExpenseType {
    return expenseTypeRepository.save(ExpenseType(expenseTypeRequest.name, expenseTypeRequest.identifiers, id))
  }

  fun delete(id: String) {
    expenseTypeRepository.delete(expenseTypeRepository.findById(id).get())
  }

  fun findAll(): List<ExpenseType> {
    return expenseTypeRepository.findAll()
  }

}
