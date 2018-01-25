package com.davdog.expensetracker.controller.json

import org.hibernate.validator.constraints.NotEmpty

class ExpenseTypeRequest(@field:NotEmpty val type: String, val identifiers: List<String>)
