package com.example.itechticketing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.springframework.web.bind.WebDataBinder
import java.beans.PropertyEditorSupport

@Controller
@RequestMapping("/tickets")
class TicketController(
    val ticketRepository: TicketRepository,
    val fieldDefinitionRepository: FieldDefinitionRepository
) {

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(LocalDate::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String) {
                value = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
            }
        })
    }

    // --- NEW: ENGINEER INPUT FORM ---

    @GetMapping("/create")
    fun showCreateForm(model: Model): String {
        // Fetches Brand, Lead Engineer, etc., created by Admin
        model.addAttribute("activeFields", fieldDefinitionRepository.findAll())
        return "create-ticket"
    }

    @PostMapping("/save")
    fun saveTicket(@RequestParam allParams: Map<String, String>): String {
        // 1. Create the base Ticket record
        val ticket = Ticket().apply {
            dateCreated = LocalDate.now()
            status = TicketStatus.OPEN
        }

        // 2. Save ticket first to generate the Ticket ID for Foreign Keys
        val savedTicket = ticketRepository.save(ticket)

        // 3. Collect all dynamic inputs from the form
        val activeFields = fieldDefinitionRepository.findAll()
        val values = activeFields.mapNotNull { field ->
            val submittedValue = allParams["field_${field.id}"]

            // Only save if the engineer actually typed something
            if (!submittedValue.isNullOrBlank()) {
                TicketValue(
                    ticket = savedTicket,
                    fieldDefinition = field,
                    value = submittedValue
                )
            } else null
        }.toMutableList()

        // 4. Attach values and final save
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
        val results = when {
            id != null -> ticketRepository.findById(id).map { listOf(it) }.orElse(emptyList())
            !leadEngineer.isNullOrBlank() -> {
                // In Dynamic EAV, search is slightly different.
                // For now, this finds tickets where ANY dynamic value matches the search string.
                ticketRepository.findAll().filter { ticket ->
                    ticket.dynamicValues.any { it.value.contains(leadEngineer, ignoreCase = true) }
                }
            }
            startDate != null && endDate != null -> ticketRepository.findByDateCreatedBetween(startDate, endDate)
            else -> ticketRepository.findAll()
        }

        model.addAttribute("tickets", results)
        model.addAttribute("totalTickets", results.size)
        model.addAttribute("searchQuery", leadEngineer ?: id ?: "$startDate to $endDate")
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
        model.addAttribute("statusList", TicketStatus.values())
        return "edit"
    }

    @PostMapping("/update")
    fun updateTicket(@ModelAttribute ticket: Ticket): String {
        // Note: For dynamic fields, you'd loop through allParams again here
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
}