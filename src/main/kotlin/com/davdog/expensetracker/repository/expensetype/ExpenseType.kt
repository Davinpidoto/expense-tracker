package com.davdog.expensetracker.repository.expensetype

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class ExpenseType (val name: String = "", val identifiers: List<String>? = null, @Id val id: String = UUID.randomUUID().toString())

