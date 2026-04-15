package com.example.itechticketing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.springframework.web.bind.WebDataBinder
import java.beans.PropertyEditorSupport
import org.springframework.format.annotation.DateTimeFormat

@Controller
@RequestMapping("/tickets")
class TicketController(
    val ticketRepository: TicketRepository,
    val fieldDefinitionRepository: FieldDefinitionRepository
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(LocalDate::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String?) {
                // FIX: Check for null or empty string before parsing
                if (text.isNullOrBlank()) {
                    value = null
                } else {
                    try {
                        value = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
                    } catch (e: Exception) {
                        value = null
                    }
                }
            }
        })
    }

    @GetMapping("/create")
    fun showCreateForm(model: Model): String {
        val allFields = fieldDefinitionRepository.findAll()

        val formOrder = listOf(
            "Brand",
            "Parts Number",
            "DC Room",
            "Tickets Work Order",
            "Lead Engineer",
            "Other Engineer",
            "Manpower",
            "Start Time",
            "End Time"
        )

        val sortedFields = formOrder.mapNotNull { label ->
            allFields.find { it.label.trim().equals(label, ignoreCase = true) }
        }

        model.addAttribute("activeFields", sortedFields)
        return "create-ticket"
    }

    @PostMapping("/save")
    fun saveTicket(@RequestParam allParams: Map<String, String>): String {
        val ticket = Ticket().apply {
            dateCreated = LocalDate.now()
            status = TicketStatus.OPEN
        }

        val savedTicket = ticketRepository.save(ticket)

        val activeFields = fieldDefinitionRepository.findAll()
        val values = activeFields.mapNotNull { field ->
            val submittedValue = allParams["field_${field.id}"]

            if (!submittedValue.isNullOrBlank()) {
                TicketValue(
                    ticket = savedTicket,
                    fieldDefinition = field,
                    value = submittedValue
                )
            } else null
        }.toMutableList()

        savedTicket.dynamicValues = values
        ticketRepository.save(savedTicket)

        return "redirect:/tickets/search"
    }

    // --- SEARCH & VIEW LOGIC ---

    @GetMapping("/search")
    fun searchTickets(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) leadEngineer: String?,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        model: Model
    ): String {
        var tickets = ticketRepository.findAll()

        // 1. Filter by ID
        if (id != null) {
            tickets = tickets.filter { it.ticketNo == id }
        }

        // 2. Filter by Lead Engineer (Dynamic Value check)
        if (!leadEngineer.isNullOrBlank()) {
            tickets = tickets.filter { ticket ->
                ticket.dynamicValues.any {
                    it.fieldDefinition.label.equals("Lead Engineer", ignoreCase = true) &&
                            it.value.contains(leadEngineer, ignoreCase = true)
                }
            }
        }

        // 3. Filter by Date Range
        if (startDate != null) {
            tickets = tickets.filter { !it.dateCreated.isBefore(startDate) }
        }
        if (endDate != null) {
            tickets = tickets.filter { !it.dateCreated.isAfter(endDate) }
        }

        model.addAttribute("tickets", tickets)
        model.addAttribute("totalTickets", tickets.size)
        return "search"
    }

    @GetMapping("/details/{id}")
    fun getTicketDetails(@PathVariable id: Long, model: Model): String {
        val ticket = ticketRepository.findById(id).orElse(null)
        if (ticket != null) {
            model.addAttribute("ticket", ticket)
            return "details"
        }
        return "redirect:/tickets/search"
    }

    // --- EDIT & DELETE LOGIC ---

    @GetMapping("/edit/{id}")
    fun showEditForm(@PathVariable id: Long, model: Model): String {
        val ticket = ticketRepository.findById(id).orElseThrow()
        model.addAttribute("ticket", ticket)
        model.addAttribute("statusList", TicketStatus.entries)
        return "edit"
    }

    @PostMapping("/update")
    fun updateTicket(@ModelAttribute ticket: Ticket): String {
        ticketRepository.save(ticket)
        return "redirect:/tickets/details/${ticket.ticketNo}"
    }

    @PostMapping("/delete/{id}")
    fun deleteTicket(@PathVariable id: Long): String {
        ticketRepository.deleteById(id)
        return "redirect:/tickets/search"
    }

    @GetMapping("/api")
    @ResponseBody
    fun getAllTicketsApi(): List<Ticket> = ticketRepository.findAll()

    // Helper for Live Status
    fun getLiveStatus(ticket: Ticket): String {
        val now = java.time.LocalTime.now()

        val startStr = ticket.dynamicValues.find { it.fieldDefinition.label.contains("Start Time") }?.value
        val endStr = ticket.dynamicValues.find { it.fieldDefinition.label.contains("End Time") }?.value

        return try {
            val start = java.time.LocalTime.parse(startStr)
            val end = java.time.LocalTime.parse(endStr)

            when {
                now.isBefore(start) -> "OPEN"
                now.isAfter(start) && now.isBefore(end) -> "IN PROGRESS"
                now.isAfter(end) -> "COMPLETED"
                else -> "UNKNOWN"
            }
        } catch (e: Exception) {
            "PENDING DATA"
        }
    }
}