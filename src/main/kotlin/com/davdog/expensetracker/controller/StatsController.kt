package com.davdog.expensetracker.controller

import com.davdog.expensetracker.controller.json.StatsResponse
import com.davdog.expensetracker.service.StatsService
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@CrossOrigin(origins = ["http://localhost:8081"])
class StatsController (val statsService: StatsService) {

    @GetMapping("/stats")
    fun getStats(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>): StatsResponse {
        return statsService.getStats(from ,to)
    }

    @RequestMapping( "/csv")
    fun downloadCSV(@RequestParam("from") from: Optional<String>, @RequestParam("to") to: Optional<String>, response: HttpServletResponse) {
        response.contentType = "text/csv"
        response.setHeader("Content-disposition", "attachment;filename=stats.csv")
        val iterator = statsService.getStatsForCsv(from, to).iterator()
        while (iterator.hasNext()) {
            response.outputStream.print(iterator.next())
        }
        response.outputStream.flush()
    }

}