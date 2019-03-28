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
    private val statsService = StatsService(expenseRepository)
    private val expenses = ArrayList<Expense>()
    private val from = Optional.of("1/1/2019")
    private val to  = Optional.of("31/12/2019")
    private val empty = Optional.empty<String>()
    private val NEW_LINE = "\n"

    @Before
    fun setUp() {
        whenever(expenseRepository.getExpenses(from, to)).thenReturn(expenses)
    }

    @Test
    fun getExpenses_multipleExpensesFromTheSameType() {
        expenses.add(aTransaction().build())
        expenses.add(aTransaction().build())

        val response = statsService.getStats(from, to)

        assertThat(response.from).isEqualTo(from.get())
        assertThat(response.to).isEqualTo(to.get())
        assertThat(response.stats.size).isEqualTo(1)
        assertThat(response.stats["Phone"]).isEqualTo("$200")
    }

    @Test
    fun getExpenses_expensesFromTwoTypes() {
        expenses.add(aTransaction().withExpenseType("Car").build())
        expenses.add(aTransaction().build())

        val response = statsService.getStats(from, to)

        assertThat(response.from).isEqualTo(from.get())
        assertThat(response.to).isEqualTo(to.get())
        assertThat(response.stats.size).isEqualTo(2)
        assertThat(response.stats["Car"]).isEqualTo("$100")
        assertThat(response.stats["Phone"]).isEqualTo("$100")
    }

    @Test
    fun getExpenses_emptyDates() {
        whenever(expenseRepository.getExpenses(empty, empty)).thenReturn(expenses)
        expenses.add(aTransaction().build())

        val response = statsService.getStats(empty, empty)

        assertThat(response.from).isEqualTo("")
        assertThat(response.to).isEqualTo("")
        assertThat(response.stats.size).isEqualTo(1)
        assertThat(response.stats["Phone"]).isEqualTo("$100")
    }

    @Test
    fun getStatsForCsv() {
        expenses.add(aTransaction().build())
        expenses.add(aTransaction().withExpenseType("Car").build())

        val response = statsService.getStatsForCsv(from, to)

        assertThat(response.size).isEqualTo(6)
        assertThat(response[0]).isEqualTo("Category,Amount")
        assertThat(response[1]).isEqualTo(NEW_LINE)
        assertThat(response[2]).isEqualTo("Car,100")
        assertThat(response[3]).isEqualTo(NEW_LINE)
        assertThat(response[4]).isEqualTo("Phone,100")
        assertThat(response[5]).isEqualTo(NEW_LINE)
    }

}