package com.davdog.expensetracker.service

import com.davdog.expensetracker.controller.json.StatsResponse
import com.davdog.expensetracker.repository.expense.ExpenseRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class StatsService(val expenseRepository: ExpenseRepository) {

    fun getStats(from: Optional<String>, to: Optional<String>): StatsResponse {
        val formattedResponse : MutableMap<String, String> = HashMap()
        getRawStats(from, to).forEach{formattedResponse[it.key]=formatAmount(it.value)}
        return StatsResponse(from.orElse(""), to.orElse(""), formattedResponse)
    }

    fun getStatsForCsv(from: Optional<String>, to: Optional<String>): ArrayList<String> {
        val rows = ArrayList<String>()
        rows.add("Category,Amount")
        rows.add("\n")

        getRawStats(from, to).forEach{
            rows.add("${it.key},${it.value}")
            rows.add("\n")
        }
        return rows
    }

    private fun getRawStats(from: Optional<String>, to: Optional<String>): Map<String, BigDecimal> {
        val expenses = expenseRepository.getExpenses(from, to)
        val map : MutableMap<String, BigDecimal> = HashMap()
        val groups = expenses.groupBy({ it.expenseType.name }, {it})
        groups.forEach{k, v -> map[k] = v.map{it.amount}.reduce{a, b -> a.plus(b)} }
        return map
    }

    private fun formatAmount(amount: BigDecimal) : String {
        return "$${amount}"
    }
}