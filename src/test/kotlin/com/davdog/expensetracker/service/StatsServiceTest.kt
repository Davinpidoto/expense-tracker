package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expense.Expense
import com.davdog.expensetracker.repository.expense.ExpenseRepository
import com.davdog.expensetracker.util.aTransaction
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.mock.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class StatsServiceTest {

    private val expenseRepository: ExpenseRepository = mock()
    private val empty = Optional.empty<String>()
    private val statsService = StatsService(expenseRepository)
    private val expenses = ArrayList<Expense>()

    @Before
    fun setUp() {
        whenever(expenseRepository.getExpenses(empty, empty)).thenReturn(expenses)
    }

    @Test
    fun getExpenses_multipleExpensesFromTheSameType() {
        expenses.add(aTransaction().build())
        expenses.add(aTransaction().build())

        val response = statsService.getStats(empty, empty)

        assertThat(response.stats.size).isEqualTo(1)
        assertThat(response.stats["Phone"]).isEqualTo("$200")
    }

    @Test
    fun getExpenses_expensesFromTwoTypes() {
        expenses.add(aTransaction().withExpenseType("Car").build())
        expenses.add(aTransaction().build())


        val response = statsService.getStats(empty, empty)

        assertThat(response.stats.size).isEqualTo(2)
        assertThat(response.stats["Car"]).isEqualTo("$100")
        assertThat(response.stats["Phone"]).isEqualTo("$100")
    }

}