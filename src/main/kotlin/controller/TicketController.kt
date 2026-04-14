package com.example.itechticketing.controller

import com.example.itechticketing.Ticket
import com.example.itechticketing.TicketRepository
import com.example.itechticketing.TicketStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport
import java.time.format.DateTimeFormatter

@Controller
@RequestMapping("/tickets")
class TicketController(val ticketRepository: TicketRepository) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        // This helps Spring convert the HTML date string (yyyy-MM-dd) to LocalDate
        binder.registerCustomEditor(LocalDate::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String) {
                value = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
            }
        })
    }

    // This handles the HTML page: http://localhost:8080/tickets/search
    @GetMapping("/search")
    fun searchTickets(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) leadEngineer: String?,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        model: Model
    ): String {

        val results = when {
            id != null -> ticketRepository.findById(id).map { listOf(it) }.orElse(emptyList())
            !leadEngineer.isNullOrBlank() -> ticketRepository.findByLeadEngineerContainingIgnoreCase(leadEngineer)
            startDate != null && endDate != null -> ticketRepository.findByDateBetween(startDate, endDate)
            else -> ticketRepository.findAll()
        }

        model.addAttribute("tickets", results)
        model.addAttribute("totalTickets", results.size)
        // This helps the UI show specific messages like "Found 5 tickets for Member A"
        model.addAttribute("searchQuery", leadEngineer ?: id ?: "$startDate to $endDate")
        return "search"
    }



    @GetMapping("/details/{id}")
    fun getTicketDetails(@PathVariable id: Long, model: Model): String {
        // 1. Look for the ticket by ID in the database
        val ticketOptional = ticketRepository.findById(id)

        if (ticketOptional.isPresent) {
            // 2. If found, send the ticket data to the HTML
            model.addAttribute("ticket", ticketOptional.get())
            return "details" // This looks for templates/details.html
        } else {
            // 3. If not found (wrong ID), go back to search or show an error
            return "redirect:/tickets/search"
        }
    }
    // This handles raw data (JSON) if you want to test: http://localhost:8080/tickets/api
    @GetMapping("/api")
    @ResponseBody // Add this so it returns DATA, not a HTML page
    fun getAllTicketsApi(): List<Ticket> {
        return ticketRepository.findAll()
    }

    // DELETE: Remove a ticket
    @PostMapping("/delete/{id}")
    fun deleteTicket(@PathVariable id: Long): String {
        ticketRepository.deleteById(id)
        return "redirect:/tickets/search" // Go back to search after deleting
    }

    // EDIT: Open the edit form
    @GetMapping("/edit/{id}")
    fun showEditForm(@PathVariable id: Long, model: Model): String {
        val ticket = ticketRepository.findById(id).orElseThrow()
        model.addAttribute("ticket", ticket)
        model.addAttribute("statusList", TicketStatus.values()) // For the dropdown
        return "edit" // This will look for edit.html
    }

    // UPDATE: Save the edited changes
    @PostMapping("/update")
    fun updateTicket(@ModelAttribute ticket: Ticket): String {
        ticketRepository.save(ticket)
        return "redirect:/tickets/details/${ticket.ticketNo}"
    }
}