package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.PostConstruct

@Component
class JsonLoader (val expenseTypeRepository: ExpenseTypeRepository) {

  val DATA_FILE_NAME = "data.json"
  val mapper = ObjectMapper()

  @PostConstruct
  fun load() {
    val json = getFile(DATA_FILE_NAME)
    val types = mapper.readValue(json, ExpenseTypes::class.java)
    types.expenseTypes.forEach {
      val expenseTypeFromDb : ExpenseType
      try {
        expenseTypeFromDb = expenseTypeRepository.findByName(it.name)
        expenseTypeRepository.save(ExpenseType(it.name, it.identifiers, expenseTypeFromDb.id))
      } catch (e : EmptyResultDataAccessException) {
        expenseTypeRepository.save(ExpenseType(it.name, it.identifiers, it.id))
      }
    }
  }

  private fun getFile(fileName: String): String {

    val result = StringBuilder("")
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource(fileName)!!.file)

    try {
      Scanner(file).use{scanner ->
        while (scanner.hasNextLine()) {
          val line = scanner.nextLine()
          result.append(line).append("\n")
        }
        scanner.close()
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return result.toString()
  }

  class ExpenseTypes {
    val expenseTypes: List<ExpenseType> = ArrayList()
  }

}


