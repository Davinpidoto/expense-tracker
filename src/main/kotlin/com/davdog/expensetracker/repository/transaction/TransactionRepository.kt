package com.davdog.expensetracker.repository.transaction

import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, String> {
}
