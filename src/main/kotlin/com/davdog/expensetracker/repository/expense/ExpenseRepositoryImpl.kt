package com.davdog.expensetracker.repository.expense

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ExpenseRepositoryImpl (val mongoTemplate: MongoTemplate): ExpenseRepositoryCustom {

  private val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

  override fun getExpenses(from: Optional<String>, to: Optional<String>) : List<Expense> {
    val query = Query()

    if (from.isPresent && !to.isPresent) {
      val fromDate = LocalDate.parse(from.get(), formatter)
      query.addCriteria(Criteria.where("transactionDate").gte(fromDate))
    }

    if (to.isPresent && !from.isPresent) {
      val toDate = LocalDate.parse(to.get(), formatter)
      query.addCriteria(Criteria.where("transactionDate").lte(toDate))
    }

    if (to.isPresent && from.isPresent) {
      val toDate = LocalDate.parse(to.get(), formatter)
      val fromDate = LocalDate.parse(from.get(), formatter)
      query.addCriteria(Criteria.where("transactionDate").gte(fromDate).lte(toDate))
    }

    return mongoTemplate.find(query, Expense::class.java).sortedBy { it.transactionDate }
  }

}