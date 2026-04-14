package com.example.itechticketing

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

@Component
class DataInitializer(val ticketRepository: TicketRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        // Check if DB is empty
        if (ticketRepository.count() == 0L) {
            val tickets = listOf(
                Ticket(
                    date = LocalDate.of(2026, 4, 10),
                    brand = "Dell",
                    partsNumber = "PN-10023", // Matches var in Ticket.kt
                    dcRoom = "DC-Shanghai",
                    ticketTitle = "Server Maintenance",
                    leadEngineer = "Member A",
                    otherEngineer = "Member B",
                    manpower = 1.0,
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(17, 0),
                    status = TicketStatus.COMPLETED
                ),
                Ticket(
                    date = LocalDate.of(2026, 4, 12),
                    brand = "HP",
                    partsNumber = "PN-9981",
                    dcRoom = "DC-Beijing",
                    ticketTitle = "Disk Replacement",
                    leadEngineer = "Member B",
                    otherEngineer = "Member C",
                    manpower = 0.5,
                    startTime = LocalTime.of(10, 0),
                    endTime = LocalTime.of(14, 0),
                    status = TicketStatus.IN_PROGRESS
                ),
                Ticket(
                    date = LocalDate.of(2026, 4, 14),
                    brand = "Lenovo",
                    partsNumber = "PN-4452",
                    dcRoom = "DC-Shanghai",
                    ticketTitle = "Network Configuration",
                    leadEngineer = "Member A",
                    otherEngineer = null,
                    manpower = 0.2,
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(15, 30),
                    status = TicketStatus.OPEN
                )
            )

            ticketRepository.saveAll(tickets)
            println(">> Dummy data has been initialized in MS SQL!")
        } else {
            println(">> Database already has data (${ticketRepository.count()} rows found).")
        }
    }
}