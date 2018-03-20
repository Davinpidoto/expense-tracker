package com.davdog.expensetracker.service

import com.davdog.expensetracker.repository.expensetype.ExpenseType
import com.davdog.expensetracker.repository.expensetype.ExpenseTypeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.PostConstruct

@Component
class JsonLoader (val expenseTypeRepository: ExpenseTypeRepository) {

  val dataFileName = "data.json"
  val mapper = ObjectMapper()

  @PostConstruct
  fun load() {
    val json = getFile(dataFileName)
    val types = mapper.readValue(json, ExpenseTypes::class.java)
    types.expenseTypes.forEach {
      val expenseTypeFromDb = expenseTypeRepository.findByType(it.type)
      if (expenseTypeFromDb == null) {
        expenseTypeRepository.save(ExpenseType(it.type, it.identifiers, it.id))
      } else {
        expenseTypeRepository.save(ExpenseType(it.type, it.identifiers, expenseTypeFromDb.id))
      }
    }
  }

  private fun getFile(fileName: String): String {

    val result = StringBuilder("")
    val classLoader = javaClass.classLoader
    val file = File(classLoader.getResource(fileName)!!.file)

    try {
      Scanner(file).use({ scanner ->
        while (scanner.hasNextLine()) {
          val line = scanner.nextLine()
          result.append(line).append("\n")
        }
        scanner.close()
      })
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return result.toString()
  }

  class ExpenseTypes {
    val expenseTypes: List<ExpenseType> = ArrayList()
  }

}


