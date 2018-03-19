package com.davdog.expensetracker.repository.expensetype

import org.springframework.data.mongodb.repository.MongoRepository

interface ExpenseTypeRepository: MongoRepository<ExpenseType, String> {

  fun findByType(type: String): ExpenseType?

}
