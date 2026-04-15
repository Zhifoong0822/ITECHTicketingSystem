package com.example.itechticketing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/summary")
class SummaryController(
    private val summaryService: SummaryService
) {

    // Serves the HTML page
    @GetMapping
    fun getSummaryPage(model: Model): String {
        val summaryData = summaryService.getSummary() //fetch summary data using summaryService
        model.addAttribute("summary", summaryData)
        return "summary" // refers to src/main/resources/templates/summary.html (Thymeleaf)
    }

    // API endpoint to return JSON (for fetch)
    @GetMapping("/data")
    @ResponseBody
    fun getSummaryData(): SummaryDTO {
        return summaryService.getSummary()
    }
}
