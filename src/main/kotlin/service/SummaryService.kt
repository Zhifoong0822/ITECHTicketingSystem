package com.example.itechticketing

import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class SummaryService(
    private val ticketRepository: TicketRepository
) {
    fun getSummary(): SummaryDTO {
        val allTickets = ticketRepository.findAll()

        // 1. Calculate Header Stats
        val totalTickets = allTickets.size
        val activeTickets = allTickets.count { it.status == TicketStatus.OPEN || it.status == TicketStatus.IN_PROGRESS }
        val completedTickets = allTickets.count { it.status == TicketStatus.COMPLETED }

        // Sum up manpower from dynamic values (assuming you have a 'Manpower' field)
        val totalManpower = allTickets.flatMap { it.dynamicValues }
            .filter { it.fieldDefinition.label.equals("Manpower", ignoreCase = true) }
            .sumOf { it.value.toIntOrNull() ?: 0 }

        // 2. Map Recent Tickets (Top 5)
        val recentTickets = allTickets.sortedByDescending { it.ticketNo }.take(5).map { ticket ->
            TicketRowDTO(
                ticketNo = ticket.ticketNo ?: 0,
                date = ticket.dateCreated.toString(),
                // Helper function to find dynamic values by label
                brand = getDynamicValue(ticket, "Brand"),
                leadEngineer = getDynamicValue(ticket, "Lead Engineer"),
                manpower = getDynamicValue(ticket, "Manpower").toIntOrNull() ?: 0,
                status = ticket.status.toString()
            )
        }

        // 3. Engineer Summary (Grouping by Lead Engineer)
        val engineerSummary = allTickets.flatMap { it.dynamicValues }
            // 1. Find all "Lead Engineer" entries that aren't empty
            .filter { it.fieldDefinition.label.equals("Lead Engineer", ignoreCase = true) && it.value.isNotBlank() }
            // 2. Group them by the Engineer's Name (e.g., "Member A", "Member B")
            .groupBy { it.value }
            // 3. Convert each group into your DTO
            .map { (name, values) ->
                // 'values' is a list of every time this engineer's name appeared in a ticket
                val ticketCountForEngineer = values.size

                // Calculate manpower specifically for THIS engineer's tickets
                val totalManpowerForEngineer = values.map { it.ticket } // Get the tickets for this engineer
                    .flatMap { it.dynamicValues }
                    .filter { it.fieldDefinition.label.equals("Manpower", ignoreCase = true) }
                    .sumOf { it.value.toIntOrNull() ?: 0 }

                EngineerSummaryDTO(
                    name = name,
                    ticketCount = ticketCountForEngineer, // Matches your DTO property name
                    totalManpower = totalManpowerForEngineer
                )
            }

        // 4. Unique Brands list
        val brands = allTickets.flatMap { it.dynamicValues }
            .filter { it.fieldDefinition.label.equals("Brand", ignoreCase = true) }
            .map { it.value }
            .distinct()

        return SummaryDTO(
            totalTickets = totalTickets,
            totalManpower = totalManpower,
            activeTickets = activeTickets,
            completedTickets = completedTickets,
            recentTickets = recentTickets,
            engineerSummary = engineerSummary,
            brands = brands
        )
    }

    // Helper function to safely extract values from the EAV list
    private fun getDynamicValue(ticket: Ticket, label: String): String {
        return ticket.dynamicValues
            .find { it.fieldDefinition.label.equals(label, ignoreCase = true) }
            ?.value ?: "N/A"
    }
}