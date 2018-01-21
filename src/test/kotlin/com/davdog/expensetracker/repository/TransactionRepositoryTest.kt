package com.davdog.expensetracker.repository

import com.davdog.expensetracker.repository.transaction.Transaction
import com.davdog.expensetracker.repository.transaction.TransactionRepository
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
class TransactionRepositoryTest {

  @Autowired
  lateinit var repository: TransactionRepository

  val transactionBuilder = TransactionBuilder()

  @Test
  fun contextLoads() {
    repository.deleteAll()
    repository.save(transactionBuilder.aTransaction().build())
    val transactions: List<Transaction> = repository.findAll()
    Assert.assertThat(transactions[0].description, Is.`is`("Telstra Phone Bill"))
  }

}
