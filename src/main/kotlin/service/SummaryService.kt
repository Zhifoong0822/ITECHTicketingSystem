package com.example.itechticketing

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SummaryService(
    private val ticketRepository: TicketRepository
) {
    fun getSummary(): SummaryDTO {
        // 1. Create a few dummy Recent Tickets
        val dummyRecentTickets = listOf(
            TicketRowDTO(1001, "2026-04-14", "Dell", "Gigabyte", 1, "OPEN"),
            TicketRowDTO(1002, "2026-04-15", "HP", "Member A", 2, "IN_PROGRESS"),
            TicketRowDTO(1003, "2026-04-15", "Cisco", "Member B", 5, "COMPLETED")
        )

        // 2. Create a few dummy Engineer Summaries
        val dummyEngineers = listOf(
            EngineerSummaryDTO("Gigabyte", 12, 15),
            EngineerSummaryDTO("Member A", 8, 10),
            EngineerSummaryDTO("Member B", 5, 4)
        )

        // 3. Create dummy Brand list
        val dummyBrands = listOf("Dell", "HP", "Cisco", "Lenovo", "Apple")

        // 4. Return the full object
        return SummaryDTO(
            totalTickets = 100,
            totalManpower = 250,
            activeTickets = 25,
            completedTickets = 58,
            recentTickets = dummyRecentTickets,
            engineerSummary = dummyEngineers,
            brands = dummyBrands
        )
    }
}