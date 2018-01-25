package com.davdog.expensetracker.repository.expense

import org.springframework.data.mongodb.repository.MongoRepository

interface ExpenseRepository: MongoRepository<Expense, String> {

}
