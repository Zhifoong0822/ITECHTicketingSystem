package com.example.itechticketing.controller //

import org.springframework.ui.Model
import com.example.itechticketing.TicketRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
@RequestMapping("/tickets")
class TicketController(val ticketRepository: TicketRepository) {

    @GetMapping("/search")
    fun searchTickets(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) leadEngineer: String?,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        model: Model
    ): String {

        // Logical Search Flow
        val results = when {
            id != null -> ticketRepository.findById(id).map { listOf(it) }.orElse(emptyList())
            leadEngineer != null && leadEngineer.isNotBlank() -> ticketRepository.findByLeadEngineerContainingIgnoreCase(leadEngineer)
            startDate != null && endDate != null -> ticketRepository.findByDateBetween(startDate, endDate)
            else -> ticketRepository.findAll()
        }

        model.addAttribute("tickets", results)
        model.addAttribute("totalTickets", results.size)
        return "search" // This opens search.html
    }
}