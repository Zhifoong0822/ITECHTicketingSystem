package com.example.itechticketing

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    val ticketRepository: TicketRepository,
    val fieldRepo: FieldDefinitionRepository,
    val valueRepo: TicketValueRepository,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        // 1. Initialize Users
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(
                    userName = "admin",
                    userPassword = passwordEncoder.encode("admin123") ?: "",
                    fullName = "System Administrator",
                    role = UserRole.ADMIN
                ),
                User(
                    userName = "engineer",
                    userPassword = passwordEncoder.encode("eng123") ?: "",
                    fullName = "John Engineer",
                    role = UserRole.ENGINEER
                ),
                User(
                    userName = "china",
                    userPassword = passwordEncoder.encode("china123") ?: "",
                    fullName = "China Viewer",
                    role = UserRole.CHINA
                ),
                User(
                    userName = "sarah.eng",
                    userPassword = passwordEncoder.encode("eng123") ?: "",
                    fullName = "Sarah Chen",
                    role = UserRole.ENGINEER
                ),
                User(
                    userName = "mike.eng",
                    userPassword = passwordEncoder.encode("eng123") ?: "",
                    fullName = "Mike Johnson",
                    role = UserRole.ENGINEER
                )
            )
            userRepository.saveAll(users)
            println(">> Users initialized! Default credentials created.")
        }

        // 2. Initialize Field Definitions
        if (fieldRepo.count() == 0L) {
            val fields = listOf(
                FieldDefinition(label = "Brand", fieldType = "TEXT"),
                FieldDefinition(label = "DC Room", fieldType = "TEXT"),
                FieldDefinition(label = "Parts Number", fieldType = "TEXT"),
                FieldDefinition(label = "Lead Engineer", fieldType = "TEXT"),
                FieldDefinition(label = "Other Engineer", fieldType = "TEXT"),
                FieldDefinition(label = "Manpower", fieldType = "NUMBER"),
                FieldDefinition(label = "Start Time", fieldType = "TIME"),
                FieldDefinition(label = "End Time", fieldType = "TIME")
            )
            fieldRepo.saveAll(fields)
            println(">> Field Definitions initialized!")
        }

        // 3. Initialize Dummy Tickets
        if (ticketRepository.count() == 0L) {
            val allFields = fieldRepo.findAll()

            // Use requireNotNull with explicit nullable type
            val brandField = requireNotNull(allFields.find { it.label == "Brand" }) { "Brand field not found" }
            val dcField = requireNotNull(allFields.find { it.label == "DC Room" }) { "DC Room field not found" }
            val leadEngField = requireNotNull(allFields.find { it.label == "Lead Engineer" }) { "Lead Engineer field not found" }
            val otherEngField = requireNotNull(allFields.find { it.label == "Other Engineer" }) { "Other Engineer field not found" }
            val manpowerField = requireNotNull(allFields.find { it.label == "Manpower" }) { "Manpower field not found" }
            val startTimeField = requireNotNull(allFields.find { it.label == "Start Time" }) { "Start Time field not found" }
            val endTimeField = requireNotNull(allFields.find { it.label == "End Time" }) { "End Time field not found" }

            // Create first ticket
            val ticket1 = Ticket(
                tickets = 101, // ✅ Adding the int value for the 'tickets' field
                dateCreated = LocalDate.now(),
                status = TicketStatus.COMPLETED
            )
            ticketRepository.save(ticket1)

            val values1 = listOf(
                TicketValue(ticket = ticket1, fieldDefinition = brandField, value = "Dell"),
                TicketValue(ticket = ticket1, fieldDefinition = dcField, value = "DC-Shanghai"),
                TicketValue(ticket = ticket1, fieldDefinition = leadEngField, value = "Sarah Chen"),
                TicketValue(ticket = ticket1, fieldDefinition = otherEngField, value = "Mike Johnson"),
                TicketValue(ticket = ticket1, fieldDefinition = manpowerField, value = "3"),
                TicketValue(ticket = ticket1, fieldDefinition = startTimeField, value = "09:00"),
                TicketValue(ticket = ticket1, fieldDefinition = endTimeField, value = "17:00")
            )
            valueRepo.saveAll(values1)
            ticket1.dynamicValues = values1.toMutableList()
            ticketRepository.save(ticket1)

            val ticket2 = Ticket(
                tickets = 102, // ✅ Adding the int value
                dateCreated = LocalDate.now(),
                status = TicketStatus.IN_PROGRESS
            )
            ticketRepository.save(ticket2)

            val values2 = listOf(
                TicketValue(ticket = ticket2, fieldDefinition = brandField, value = "HP"),
                TicketValue(ticket = ticket2, fieldDefinition = dcField, value = "DC-Singapore"),
                TicketValue(ticket = ticket2, fieldDefinition = leadEngField, value = "Mike Johnson"),
                TicketValue(ticket = ticket2, fieldDefinition = otherEngField, value = "Sarah Chen"),
                TicketValue(ticket = ticket2, fieldDefinition = manpowerField, value = "2"),
                TicketValue(ticket = ticket2, fieldDefinition = startTimeField, value = "10:00"),
                TicketValue(ticket = ticket2, fieldDefinition = endTimeField, value = "18:00")
            )
            valueRepo.saveAll(values2)
            ticket2.dynamicValues = values2.toMutableList()
            ticketRepository.save(ticket2)

            println(">> Dynamic dummy data initialized!")
        }
    }
}