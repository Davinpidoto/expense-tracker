package com.davdog.expensetracker.repository

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expense.ExpenseRepository
import com.davdog.expensetracker.util.TransactionBuilder
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ExpenseRepositoryTest {

  @Autowired
  lateinit var repository: ExpenseRepository

  val transactionBuilder = TransactionBuilder()

  @Test
  fun contextLoads() {
    repository.deleteAll()
    repository.save(transactionBuilder.aTransaction().build())
    val expenses: List<Expense> = repository.findAll()
    Assert.assertThat(expenses[0].description, Is.`is`("Telstra Phone Bill"))
  }

}
