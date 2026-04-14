package com.example.itechticketing
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "tickets")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_no") // Matches your SQL Primary Key
    val ticketNo: Long = 0,

    @Column(nullable = false)
    var date: LocalDate,

    var brand: String? = null,

    @Column(name = "parts_number") // Forces snake_case
    var partsNumber: String? = null,

    @Column(name = "dc_room")
    var dcRoom: String? = null,

    @Column(name = "ticket_title")
    var ticketTitle: String? = null,

    @Column(name = "lead_engineer")
    var leadEngineer: String? = null,

    @Column(name = "other_engineer")
    var otherEngineer: String? = null,

    var manpower: Double? = null,

    @Column(name = "start_time")
    var startTime: LocalTime? = null,

    @Column(name = "end_time")
    var endTime: LocalTime? = null,

    @Enumerated(EnumType.STRING)
    var status: TicketStatus = TicketStatus.OPEN
)

enum class TicketStatus {
    OPEN, IN_PROGRESS, COMPLETED
}