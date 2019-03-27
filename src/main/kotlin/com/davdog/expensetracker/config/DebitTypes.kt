package com.davdog.expensetracker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "statement.debit")
class DebitTypes {

    lateinit var types :  MutableList<String>
}