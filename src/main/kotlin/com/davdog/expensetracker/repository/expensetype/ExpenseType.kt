package com.davdog.expensetracker.repository.expensetype

import org.springframework.data.annotation.Id
import java.util.*

class ExpenseType (@Id val id: String = UUID.randomUUID().toString(),
                   val type: String)

