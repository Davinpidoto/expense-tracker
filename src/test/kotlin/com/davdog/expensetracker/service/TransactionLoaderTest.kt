package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.transaction.Transaction
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class TransactionLoaderTest {

  @Test
  fun loadTransactions() {
    val loader =  TransactionLoader()
    val transactions: List<Transaction> = loader.loadTransactions("/TransactionHistory.csv")
    assertThat(transactions[1].description, `is`("CAFE COFFEE"))
  }
}
