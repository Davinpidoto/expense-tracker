package com.davdog.expensetracker.controller.json

import org.hibernate.validator.constraints.NotEmpty

class UpdateExpenseRequest (@field:NotEmpty val expenseType: String)
