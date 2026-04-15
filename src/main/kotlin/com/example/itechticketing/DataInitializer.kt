package com.example.itechticketing

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    val ticketRepository: TicketRepository,
    val fieldRepo: FieldDefinitionRepository,
    val valueRepo: TicketValueRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        // 1. Initialize Field Definitions
        if (fieldRepo.count() == 0L) {
            val fields = listOf(
                FieldDefinition(label = "Brand", fieldType = "TEXT"),
                FieldDefinition(label = "DC Room", fieldType = "TEXT"),
                FieldDefinition(label = "Parts Number", fieldType = "TEXT"),
                // ADD THIS LINE:
                FieldDefinition(label = "Lead Engineer", fieldType = "TEXT")
            )
            fieldRepo.saveAll(fields)
            println(">> Field Definitions initialized!")
        }

        // 2. Initialize Dummy Tickets
        if (ticketRepository.count() == 0L) {
            val brandField = fieldRepo.findAll().find { it.label == "Brand" }!!
            val dcField = fieldRepo.findAll().find { it.label == "DC Room" }!!
            val leadEngField = fieldRepo.findAll().find { it.label == "Lead Engineer" }!!

            val ticket1 = Ticket(
                dateCreated = LocalDate.now(),
                status = TicketStatus.COMPLETED
            )
            ticketRepository.save(ticket1)

            val values = listOf(
                TicketValue(ticket = ticket1, fieldDefinition = brandField, value = "Dell"),
                TicketValue(ticket = ticket1, fieldDefinition = dcField, value = "DC-Shanghai"),
                // ADD THIS LINE:
                TicketValue(ticket = ticket1, fieldDefinition = leadEngField, value = "Member A")
            )
            valueRepo.saveAll(values)

            println(">> Dynamic dummy data with Lead Engineer initialized!")
        }
    }
}