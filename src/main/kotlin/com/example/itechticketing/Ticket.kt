package com.example.itechticketing
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "tickets")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val ticketNo: Long = 0,

    var dateCreated: LocalDate = LocalDate.now(),

    @Enumerated(EnumType.STRING)
    var status: TicketStatus = TicketStatus.OPEN,

    var createdBy: String? = null,
    // This is the link to all the dynamic data (Brand, Serial No, etc.)
    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var dynamicValues: MutableList<TicketValue> = mutableListOf()
)

enum class TicketStatus {
    OPEN, IN_PROGRESS, COMPLETED
}